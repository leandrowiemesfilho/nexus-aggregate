package com.aggregate.nexus.event;

import com.aggregate.nexus.domain.AggregatedQuoteResponse;
import com.aggregate.nexus.model.QuoteHistory;
import com.aggregate.nexus.repository.QuoteHistoryRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Component responsible for listening to NewQuoteEvents and persisting them
 * to the historical database. This separation ensures that persistence
 * doesn't block the main request/response cycle.
 */
@Component
public class QuotePersistenceEventListener {
    private final QuoteHistoryRepository quoteHistoryRepository;

    /**
     * Constructs a new QuotePersistenceEventListener.
     *
     * @param quoteHistoryRepository the repository used for persisting quote entities
     */
    public QuotePersistenceEventListener(final QuoteHistoryRepository quoteHistoryRepository) {
        this.quoteHistoryRepository = quoteHistoryRepository;
    }

    /**
     * Asynchronously handles NewQuoteEvents by persisting the quote data to PostgreSQL.
     * This method is executed in a separate thread pool to avoid blocking the main application.
     *
     * @param event the NewQuoteEvent containing the quote data to persist
     */
    @Async
    @EventListener
    @Transactional
    public void eventHandler(final NewQuoteEvent event) {
        final QuoteHistory quoteHistory = new QuoteHistory();
        final AggregatedQuoteResponse quoteResponse = event.getQuoteResponse();

        quoteHistory.setSymbol(quoteResponse.symbol());
        quoteHistory.setTimestamp(Instant.now());
        quoteHistory.setAggregatedPrice(BigDecimal.valueOf(quoteResponse.lastPrice()));

        this.quoteHistoryRepository.save(quoteHistory);
    }
}
