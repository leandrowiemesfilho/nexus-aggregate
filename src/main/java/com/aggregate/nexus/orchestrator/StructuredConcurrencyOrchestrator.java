package com.aggregate.nexus.orchestrator;

import com.aggregate.nexus.domain.AggregatedQuoteResponse;
import com.aggregate.nexus.domain.MarketData;
import com.aggregate.nexus.config.SourceConfig;
import com.aggregate.nexus.service.DataAggregationService;
import com.aggregate.nexus.service.QuoteCachingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.StructuredTaskScope.ShutdownOnFailure;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.stream.Collectors;

@Component
public class StructuredConcurrencyOrchestrator {
    private final List<SourceConfig> sources;
    private final QuoteCachingService cachingService;
    private final DataAggregationService dataAggregationService;

    @Autowired
    public StructuredConcurrencyOrchestrator(final List<SourceConfig> sources,
                                             final QuoteCachingService cachingService,
                                             final DataAggregationService dataAggregationService) {
        this.sources = sources;
        this.cachingService = cachingService;
        this.dataAggregationService = dataAggregationService;
    }

    public AggregatedQuoteResponse getAggregatedQuote(final String ticker) throws InterruptedException {
        final Optional<AggregatedQuoteResponse> cachedQuote = this.cachingService.getCachedQuote(ticker);

        if (cachedQuote.isPresent()) {
            return cachedQuote.get();
        }

        final AggregatedQuoteResponse freshQuote = fetchFreshQuote(ticker);

        this.cachingService.cacheQuote(ticker, freshQuote);

        return freshQuote;
    }

    private AggregatedQuoteResponse fetchFreshQuote(String ticker) throws InterruptedException {
        // A ShutdownOnFailure policy is chosen: if any subtask fails, all others are cancelled.
        try (final ShutdownOnFailure scope = new ShutdownOnFailure()) {
            // Launch all subtasks to fetch data from each enabled source
            final List<Subtask<MarketData>> subtasks = new ArrayList<>();

            for (final SourceConfig source : this.sources) {
                if (source.enabled()) {
                    // Fork a new virtual thread for each source.
                    // The Supplier (() -> ...) is the work to be done
                    final Subtask<MarketData> subtask = scope.fork(() ->
                            this.dataAggregationService.fetchFromSource(source, ticker).get()
                    );
                    subtasks.add(subtask);
                }
            }

            // Wait for all subtasks to finish OR for one to fail
            scope.join();
            // If any subtask failed, throw the exception. This cancels other running subtasks.
            scope.throwIfFailed(e -> new RuntimeException("Failed to fetch data from all sources", e));

            // Compose the results from all successful subtasks.
            final List<MarketData> result = subtasks.stream()
                    .map(Subtask::get)
                    .toList();

            return aggregateResult(result, ticker);
        }
    }

    private AggregatedQuoteResponse aggregateResult(final List<MarketData> result, final String ticker) {
        // Simple aggregation: average price from all sources.
        final BigDecimal averagePrice = result.stream()
                .map(MarketData::currentPrice)
                .findAny() //TODO: calculate real average
                .orElse(BigDecimal.ZERO);

        // Map the results to show the price from each source.
        final Map<String, BigDecimal> sourceMap = result.stream()
                .collect(Collectors.toMap(MarketData::sourceName, MarketData::currentPrice));

        return new AggregatedQuoteResponse(ticker, averagePrice, sourceMap);
    }
}
