package com.aggregate.nexus.config;

import com.aggregate.nexus.domain.AggregatedQuoteResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Configuration class for Redis setup.
 * Defines beans for RedisTemplate with proper serialization for storing AggregatedQuoteResponse objects.
 */
@Configuration
public class RedisConfig {
    /**
     * Creates and configures a RedisTemplate for storing AggregatedQuoteResponse objects.
     * Uses JSON serialization for values and String serialization for keys.
     *
     * @param connectionFactory the Redis connection factory (auto-configured by Spring Boot)
     * @return a configured RedisTemplate instance
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(final RedisConnectionFactory connectionFactory) {
        final RedisTemplate<String, Object> template = new RedisTemplate<>();

        template.setConnectionFactory(connectionFactory);

        // Use String serializer for keys
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // Use JSON serializer for values
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        template.afterPropertiesSet();

        return template;
    }

    /**
     * Creates a specialized RedisTemplate for AggregatedQuoteResponse objects.
     * This provides type safety when working with quote data in Redis.
     *
     * @param connectionFactory the Redis connection factory
     * @return a type-safe RedisTemplate for AggregatedQuoteResponse
     */
    @Bean
    public RedisTemplate<String, AggregatedQuoteResponse> quoteRedisTemplate(RedisConnectionFactory connectionFactory) {
        final RedisTemplate<String, AggregatedQuoteResponse> template = new RedisTemplate<>();

        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        template.afterPropertiesSet();

        return template;
    }
}