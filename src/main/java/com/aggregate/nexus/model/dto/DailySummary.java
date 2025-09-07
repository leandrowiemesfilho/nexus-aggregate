package com.aggregate.nexus.model.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record DailySummary(Instant trade_date,
                           BigDecimal high_price,
                           BigDecimal low_price,
                           BigDecimal avg_price) {
}
