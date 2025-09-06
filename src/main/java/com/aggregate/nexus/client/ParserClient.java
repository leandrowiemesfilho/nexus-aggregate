package com.aggregate.nexus.client;

import com.aggregate.nexus.domain.MarketData;

public interface ParserClient {
    MarketData parseResponse(final String responseBody, final String ticker);
}
