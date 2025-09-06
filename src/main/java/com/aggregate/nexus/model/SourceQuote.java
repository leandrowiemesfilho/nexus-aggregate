package com.aggregate.nexus.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "source_quote")
public class SourceQuote {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private QuoteHistory quoteHistory;

    @Column(name = "source_name")
    private String sourceName;

    @Column(name = "price", precision = 19, scale = 4)
    private BigDecimal price;
}
