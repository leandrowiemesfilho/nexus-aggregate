package com.aggregate.nexus.domain;

import java.util.Map;
import java.util.Objects;

/**
 * Represents the final unified API response for an aggregated stock quote.
 * Contains the aggregated price and individual prices from each data source.
 */
public record AggregatedQuoteResponse(String symbol,
                                      double lastPrice,
                                      Map<String, Double> sources) {
    public AggregatedQuoteResponse {
        Objects.requireNonNull(symbol);
        sources = Map.copyOf(sources);
    }
}
