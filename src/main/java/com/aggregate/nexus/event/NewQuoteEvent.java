package com.aggregate.nexus.event;

import com.aggregate.nexus.domain.AggregatedQuoteResponse;

/**
 * Application event indicating a new quote has been successfully aggregated.
 * This event is published asynchronously after a fresh quote is retrieved
 * from external sources and is used to trigger side effects like persistence
 * and analytics without blocking the main request thread.
 */
public record NewQuoteEvent(AggregatedQuoteResponse response) {
}
