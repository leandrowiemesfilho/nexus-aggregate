package com.aggregate.nexus.controller;

import com.aggregate.nexus.service.QuoteStreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/v1/stream")
public class StreamController {
    private final QuoteStreamService quoteStreamService;

    @Autowired
    public StreamController(QuoteStreamService quoteStreamService) {
        this.quoteStreamService = quoteStreamService;
    }

    @GetMapping("/{ticker}")
    public SseEmitter stream(@PathVariable("ticker") final String ticker) {
        return this.quoteStreamService.createStream(ticker);
    }
}
