package com.aggregate.nexus.client.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FinnhubResponse(@JsonProperty("c") BigDecimal currentPrice) {
}
