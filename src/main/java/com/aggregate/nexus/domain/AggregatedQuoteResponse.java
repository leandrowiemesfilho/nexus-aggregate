package com.aggregate.nexus.domain;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

public record AggregatedQuoteResponse(String symbol,
                                      BigDecimal lastPrice,
                                      Map<String, BigDecimal> sources) {
    public AggregatedQuoteResponse {
        Objects.requireNonNull(symbol);
        sources = Map.copyOf(sources);
    }
}
