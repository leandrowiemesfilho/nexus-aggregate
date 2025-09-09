package com.aggregate.nexus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executors;

@EnableAsync
@SpringBootApplication
public class NexusAggregateApplication {

    public static void main(String[] args) {
        SpringApplication.run(NexusAggregateApplication.class, args);
    }

    /**
     * Configures the asynchronous task executor for event processing.
     * Uses virtual threads for optimal scalability of I/O-bound tasks.
     *
     * @return the configured AsyncTaskExecutor
     */
    @Bean
    public AsyncTaskExecutor asyncTaskExecutor() {
        return new TaskExecutorAdapter(Executors.newVirtualThreadPerTaskExecutor());
    }
}
