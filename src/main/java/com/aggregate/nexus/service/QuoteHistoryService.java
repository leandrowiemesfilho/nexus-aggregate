package com.aggregate.nexus.service;

import com.aggregate.nexus.model.QuoteHistory;
import com.aggregate.nexus.model.dto.DailySummary;
import com.aggregate.nexus.repository.QuoteHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuoteHistoryService {
    private final QuoteHistoryRepository quoteHistoryRepository;

    public QuoteHistoryService(final QuoteHistoryRepository quoteHistoryRepository) {
        this.quoteHistoryRepository = quoteHistoryRepository;
    }

    public List<QuoteHistory> getQuoteHistoryByTicker(final String ticker) {
        return this.quoteHistoryRepository.findBySymbolOrderByTimestampDesc(ticker);
    }

    public List<DailySummary> getDailySummary(final String ticker, final int days) {
        return this.quoteHistoryRepository.findDailySummary(ticker, days);
    }
}
