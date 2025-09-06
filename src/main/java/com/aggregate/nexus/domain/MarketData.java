package com.aggregate.nexus.domain;

import java.math.BigDecimal;

public record MarketData(String sourceName, String symbol, BigDecimal currentPrice) {
}
