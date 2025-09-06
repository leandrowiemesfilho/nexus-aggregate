package com.aggregate.nexus.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "nexus")
public class SourceConfigProperties {
    private List<SourceConfig> sources;

    public List<SourceConfig> getSources() {
        return sources;
    }

    public void setSources(List<SourceConfig> sources) {
        this.sources = sources;
    }
}
