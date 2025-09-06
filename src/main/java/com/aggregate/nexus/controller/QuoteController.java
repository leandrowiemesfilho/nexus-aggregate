package com.aggregate.nexus.controller;

import com.aggregate.nexus.domain.AggregatedQuoteResponse;
import com.aggregate.nexus.orchestrator.StructuredConcurrencyOrchestrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/quote")
public class QuoteController {
    private final StructuredConcurrencyOrchestrator orchestrator;

    @Autowired
    public QuoteController(StructuredConcurrencyOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    @GetMapping("/{ticker}")
    public ResponseEntity<AggregatedQuoteResponse> getQuote(@PathVariable("ticker") String ticker) {
        try {
            final AggregatedQuoteResponse aggregatedQuote = orchestrator.getAggregatedQuote(ticker);

            return ResponseEntity.ok(aggregatedQuote);
        } catch (InterruptedException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
