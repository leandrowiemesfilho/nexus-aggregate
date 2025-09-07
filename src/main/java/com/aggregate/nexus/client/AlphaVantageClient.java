package com.aggregate.nexus.client;

import com.aggregate.nexus.client.response.AlphaVantageResponse;
import com.aggregate.nexus.domain.MarketData;
import com.aggregate.nexus.domain.SourceQuote;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("alphaVantageClient")
public class AlphaVantageClient implements ParserClient {
    private final ObjectMapper mapper;

    @Autowired
    public AlphaVantageClient(final ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public MarketData parseResponse(final String responseBody, final String ticker) {
        try {
            final AlphaVantageResponse response = mapper.readValue(responseBody, AlphaVantageResponse.class);

            if (response.globalQuote() != null) {
                return new MarketData(SourceQuote.ALPHA_VANTAGE.getName(), ticker, response.globalQuote().price());
            }

            throw new RuntimeException("Invalid Alpha Vantage response format");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse Alpha Vantage response", e);
        }
    }
}
