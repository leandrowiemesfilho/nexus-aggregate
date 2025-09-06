package com.aggregate.nexus.integration;

import com.aggregate.nexus.config.SourceConfigProperties;
import com.aggregate.nexus.domain.MarketData;
import com.aggregate.nexus.config.SourceConfig;
import com.aggregate.nexus.service.DataAggregationService;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.concurrent.TimeUnit;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class DataAggregationServiceIT {

    @Container
    static GenericContainer<?> wiremockContainer = new GenericContainer<>(
            DockerImageName.parse("wiremock/wiremock:2.35.0")
    ).withExposedPorts(8080);

    @Autowired
    private DataAggregationService dataAggregationService;

    @Autowired
    private SourceConfigProperties configProperties;

    @Test
    void shouldFetchDataFromMockSource() {
        final SourceConfig sourceConfig = this.configProperties.getSources().getFirst();
        final String ticker = "AAPL";

        Awaitility.await()
                .atMost(5 , TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    final MarketData result = this.dataAggregationService.fetchFromSource(sourceConfig, ticker)
                            .get(20, TimeUnit.SECONDS);

                    Assertions.assertNotNull(result);
        });

    }
}
