package com.aggregate.nexus.model.dto;

import java.math.BigDecimal;
import java.util.Date;

public record DailySummary(Date trade_date,
                           BigDecimal high_price,
                           BigDecimal low_price,
                           BigDecimal avg_price) {
}
