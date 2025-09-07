package com.aggregate.nexus.client.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public record AlphaVantageResponse(@JsonProperty("Global Quote") GlobalQuote globalQuote) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record GlobalQuote(@JsonProperty("05. price") double price) {
    }
}
