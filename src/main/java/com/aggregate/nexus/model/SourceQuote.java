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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QuoteHistory getQuoteHistory() {
        return quoteHistory;
    }

    public void setQuoteHistory(QuoteHistory quoteHistory) {
        this.quoteHistory = quoteHistory;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
