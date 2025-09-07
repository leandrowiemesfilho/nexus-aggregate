package com.aggregate.nexus.domain;

import java.util.Map;
import java.util.Objects;

public record AggregatedQuoteResponse(String symbol,
                                      double lastPrice,
                                      Map<String, Double> sources) {
    public AggregatedQuoteResponse {
        Objects.requireNonNull(symbol);
        sources = Map.copyOf(sources);
    }
}
