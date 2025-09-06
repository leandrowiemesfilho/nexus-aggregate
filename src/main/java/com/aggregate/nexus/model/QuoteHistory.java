package com.aggregate.nexus.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

/**
 * Entity class representing a historical record of an aggregated stock quote.
 * Maps to the `quotes_history` table in the relational database.
 * This entity is used for storing time-series data for analytical purposes.
 */
@Entity
@Table(name = "quote_history")
public class QuoteHistory implements Serializable {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "symbol", nullable = false, length = 10)
    private String symbol;

    @Column(name = "aggregated_price", nullable = false, precision = 19, scale = 4)
    private BigDecimal aggregatedPrice;

    @Column(name = "quote_timestamp")
    private Instant timestamp;


}