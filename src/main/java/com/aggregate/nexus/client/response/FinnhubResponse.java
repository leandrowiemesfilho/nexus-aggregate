package com.aggregate.nexus.client.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FinnhubResponse(@JsonProperty("c") double currentPrice) {
}
