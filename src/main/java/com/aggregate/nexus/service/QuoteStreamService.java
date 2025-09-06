package com.aggregate.nexus.service;

import com.aggregate.nexus.domain.AggregatedQuoteResponse;
import com.aggregate.nexus.orchestrator.StructuredConcurrencyOrchestrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class QuoteStreamService {
    private final StructuredConcurrencyOrchestrator orchestrator;
    private final ScheduledExecutorService scheduler;

    @Autowired
    public QuoteStreamService(final StructuredConcurrencyOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
        this.scheduler = Executors.newScheduledThreadPool(4);
    }

    public SseEmitter createStream(final String ticker) {
        final SseEmitter emitter = new SseEmitter(30_000L);

        this.scheduler.scheduleAtFixedRate(() -> {
            CompletableFuture.runAsync(() -> {
                try {
                    final AggregatedQuoteResponse quote = this.orchestrator.getAggregatedQuote(ticker);

                    emitter.send(SseEmitter.event()
                            .id(String.valueOf(System.currentTimeMillis()))
                            .name("quote")
                            .data(quote)
                    );
                } catch (IOException | InterruptedException e) {
                    emitter.completeWithError(e);
                }
            }, Executors.newVirtualThreadPerTaskExecutor());
        }, 0, 2, TimeUnit.SECONDS);

        emitter.onCompletion(scheduler::shutdown);
        emitter.onTimeout(scheduler::shutdown);

        return emitter;
    }
}
