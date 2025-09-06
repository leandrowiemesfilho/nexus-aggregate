package com.aggregate.nexus.domain;

public enum SourceQuote {
    ALPHA_VANTAGE("alphaVantage"),
    FINNHUB("finnhub");

    private String name;

    SourceQuote(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
