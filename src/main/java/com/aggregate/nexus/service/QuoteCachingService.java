package com.aggregate.nexus.service;

import com.aggregate.nexus.domain.AggregatedQuoteResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service for caching aggregated quote responses in Redis.
 * Implements cache-aside pattern with TTL expiration.
 */
@Service
public class QuoteCachingService {
    private static final String QUOTE_PREFIX = "quote:";

    private final RedisTemplate<String, AggregatedQuoteResponse> redisTemplate;

    /**
     * Constructs a new QuoteCachingService with the specified RedisTemplate.
     *
     * @param redisTemplate the RedisTemplate configured for AggregatedQuoteResponse objects
     */
    @Autowired
    public QuoteCachingService(RedisTemplate<String, AggregatedQuoteResponse> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Caches a quote response in Redis with a time-to-live (TTL) expiration.
     *
     * @param ticker the stock ticker symbol
     * @param quote the aggregated quote response to cache
     */
    public void cacheQuote(final String ticker, final AggregatedQuoteResponse quote) {
        final String key = QUOTE_PREFIX + ticker;

        this.redisTemplate.opsForValue().set(key, quote, 60_000L);
    }

    /**
     * Retrieves a cached quote response from Redis, if it exists and hasn't expired.
     *
     * @param ticker the stock ticker symbol to look up
     * @return an Optional containing the cached quote, or empty if not found
     */
    public Optional<AggregatedQuoteResponse> getCachedQuote(final String ticker) {
        final String key = QUOTE_PREFIX + ticker;
        final AggregatedQuoteResponse quote = this.redisTemplate.opsForValue().get(key);

        return Optional.ofNullable(quote);
    }

    /**
     * Removes a cached quote from Redis.
     *
     * @param ticker the stock ticker symbol to evict from cache
     */
    public void evictCachedQuote(final String ticker) {
        final String key = QUOTE_PREFIX + ticker;

        this.redisTemplate.delete(key);
    }
}
