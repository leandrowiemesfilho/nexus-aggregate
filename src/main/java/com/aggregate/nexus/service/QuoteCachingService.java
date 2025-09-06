package com.aggregate.nexus.service;

import com.aggregate.nexus.domain.AggregatedQuoteResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuoteCachingService {
    private static final String QUOTE_PREFIX = "quote:";

    private final RedisTemplate<String, AggregatedQuoteResponse> redisTemplate;

    @Autowired
    public QuoteCachingService(RedisTemplate<String, AggregatedQuoteResponse> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void cacheQuote(final String ticker, final AggregatedQuoteResponse quote) {
        final String key = QUOTE_PREFIX + ticker;

        this.redisTemplate.opsForValue().set(key, quote, timeToLive);
    }

    public Optional<AggregatedQuoteResponse> getCachedQuote(final String ticker) {
        final String key = QUOTE_PREFIX + ticker;
        final AggregatedQuoteResponse quote = this.redisTemplate.opsForValue().get(key);

        return Optional.ofNullable(quote);
    }

    public void evictCachedQuote(final String ticker) {
        final String key = QUOTE_PREFIX + ticker;

        this.redisTemplate.delete(key);
    }
}
