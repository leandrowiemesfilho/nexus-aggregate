package com.aggregate.nexus.controller;

import com.aggregate.nexus.domain.AggregatedQuoteResponse;
import com.aggregate.nexus.model.QuoteHistory;
import com.aggregate.nexus.model.dto.DailySummary;
import com.aggregate.nexus.orchestrator.StructuredConcurrencyOrchestrator;
import com.aggregate.nexus.service.QuoteHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quote")
public class QuoteController {
    private final QuoteHistoryService quoteHistoryService;
    private final StructuredConcurrencyOrchestrator orchestrator;

    @Autowired
    public QuoteController(final QuoteHistoryService quoteHistoryService,
                           final StructuredConcurrencyOrchestrator orchestrator) {
        this.quoteHistoryService = quoteHistoryService;
        this.orchestrator = orchestrator;
    }

    @GetMapping("/{ticker}")
    public ResponseEntity<AggregatedQuoteResponse> getQuote(@PathVariable("ticker") final String ticker) {
        try {
            final AggregatedQuoteResponse aggregatedQuote = orchestrator.getAggregatedQuote(ticker);

            return ResponseEntity.ok(aggregatedQuote);
        } catch (InterruptedException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{ticker}/history")
    public ResponseEntity<List<QuoteHistory>> getQuoteHistory(@PathVariable("ticker") final String ticker) {
        final List<QuoteHistory> quoteHistoryByTicker = this.quoteHistoryService.getQuoteHistoryByTicker(ticker);

        return ResponseEntity.ok(quoteHistoryByTicker);
    }

    @GetMapping("/{ticker}/summary")
    public ResponseEntity<List<DailySummary>> getDailySummary(@PathVariable("ticker") final String ticker,
                                                              @RequestParam(value = "days", defaultValue = "7") final Integer days) {
        final List<DailySummary> dailySummary = this.quoteHistoryService.getDailySummary(ticker, days);

        return ResponseEntity.ok(dailySummary);
    }
}
