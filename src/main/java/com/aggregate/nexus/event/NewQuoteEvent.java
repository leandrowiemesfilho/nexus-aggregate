package com.aggregate.nexus.event;

import com.aggregate.nexus.domain.AggregatedQuoteResponse;

/**
 * Application event indicating a new quote has been successfully aggregated.
 * This event is published asynchronously after a fresh quote is retrieved
 * from external sources and is used to trigger side effects like persistence
 * and analytics without blocking the main request thread.
 */
public class NewQuoteEvent {
    private final AggregatedQuoteResponse response;

    /**
     * Constructs a new NewQuoteEvent.
     *
     * @param response the aggregated quote response that triggered this event
     */
    public NewQuoteEvent(final AggregatedQuoteResponse response) {
        this.response = response;
    }

    /**
     * Gets the quote response associated with this event.
     *
     * @return the AggregatedQuoteResponse containing the quote data
     */
    public AggregatedQuoteResponse getQuoteResponse() {
        return this.response;
    }
}
