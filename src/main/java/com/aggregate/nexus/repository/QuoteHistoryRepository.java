package com.aggregate.nexus.repository;

import com.aggregate.nexus.model.QuoteHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

/**
 * Repository interface for performing CRUD operations and custom queries on QuoteEntity.
 * Extends JpaRepository to inherit standard data access methods.
 */
@Repository
public interface QuoteHistoryRepository extends JpaRepository<QuoteHistory, Long> {
    /**
     * Finds all quotes for a specific symbol, ordered by timestamp descending (newest first).
     *
     * @param symbol the stock ticker symbol to search for
     * @return a list of QuoteEntity objects for the given symbol
     */
    List<QuoteHistory> findBySymbolOrderByTimestampDesc(String symbol);

    /**
     * Finds quotes for a specific symbol within a given time range.
     * Useful for historical data analysis and charting.
     *
     * @param symbol the stock ticker symbol to search for
     * @param start  the start of the time range (inclusive)
     * @param end    the end of the time range (inclusive)
     * @return a list of QuoteEntity objects within the specified time range
     */
    List<QuoteHistory> findBySymbolAndTimestampBetween(final String symbol, final Instant start, final Instant end);
}
