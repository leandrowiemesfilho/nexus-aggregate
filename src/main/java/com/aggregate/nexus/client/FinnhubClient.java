package com.aggregate.nexus.client;

import com.aggregate.nexus.client.response.FinnhubResponse;
import com.aggregate.nexus.domain.MarketData;
import com.aggregate.nexus.domain.SourceQuote;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("finnhubClient")
public class FinnhubClient implements ParserClient {
    private final ObjectMapper mapper;

    @Autowired
    public FinnhubClient(final ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public MarketData parseResponse(String responseBody, String ticker) {
        try {
            final FinnhubResponse response = mapper.readValue(responseBody, FinnhubResponse.class);

            if (response != null) {
                return new MarketData(SourceQuote.FINNHUB.getName(), ticker, response.currentPrice());
            }

            throw new RuntimeException("Invalid Finn Hub response format");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse Finn Hub response", e);
        }
    }
}
