package com.aggregate.nexus.service;

import com.aggregate.nexus.client.AlphaVantageClient;
import com.aggregate.nexus.client.FinnhubClient;
import com.aggregate.nexus.client.ParserClient;
import com.aggregate.nexus.domain.MarketData;
import com.aggregate.nexus.config.SourceConfig;
import com.aggregate.nexus.domain.SourceQuote;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class DataAggregationService {
    private final HttpClient httpClient;
    private final CircuitBreakerRegistry circuitBreakerRegistry;
    private final Map<String, ParserClient> parserClients;

    @Autowired
    public DataAggregationService(final CircuitBreakerRegistry circuitBreakerRegistry,
                                  @Qualifier("alphaVantageClient") AlphaVantageClient alphaVantageClient,
                                  @Qualifier("finnhubClient") final FinnhubClient finnhubClient) {
        this.circuitBreakerRegistry = circuitBreakerRegistry;
        this.parserClients = Map.of(
                SourceQuote.ALPHA_VANTAGE.getName(), alphaVantageClient,
                SourceQuote.FINNHUB.getName(), finnhubClient);

        this.httpClient = HttpClient.newBuilder()
                .executor(Executors.newVirtualThreadPerTaskExecutor())
                .build();
    }

    public CompletableFuture<MarketData> fetchFromSource(final SourceConfig sourceConfig, String ticker) {
        final CircuitBreaker circuitBreaker = this.circuitBreakerRegistry.circuitBreaker(sourceConfig.name());

        return CompletableFuture.supplyAsync(() ->
                circuitBreaker.executeSupplier(() -> {
                    // Build the dynamic URL
                    final String formattedUrl = sourceConfig.url().replace("{ticker}", ticker);
                    final URI uri = URI.create(formattedUrl);
                    final HttpRequest request = HttpRequest.newBuilder()
                            .uri(uri)
                            .GET()
                            .build();

                    try {
                        final HttpResponse<String> response = httpClient.send(request,
                                HttpResponse.BodyHandlers.ofString());

                        if (response.statusCode() == 200) {
                            return parseResponse(response.body(), sourceConfig.name(), ticker);
                        }

                        throw new RuntimeException(STR."HTTP error\{response.statusCode()}");
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }));
    }

    private MarketData parseResponse(final String response, final String sourceName,
                                     final String ticker) {
        final ParserClient parserClient = this.parserClients.get(sourceName);

        return parserClient.parseResponse(response, ticker);
    }
}
