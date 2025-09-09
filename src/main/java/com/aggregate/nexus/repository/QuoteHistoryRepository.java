package com.aggregate.nexus.repository;

import com.aggregate.nexus.model.QuoteHistory;
import com.aggregate.nexus.model.dto.DailySummary;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
     * Custom query to calculate daily high, low, and average prices for a symbol.
     * Demonstrates advanced querying capabilities for financial analysis.
     *
     * @param symbol the stock ticker symbol to analyze
     * @param days   the number of past days to include in the analysis
     * @return a list of Object arrays containing date, high, low, and average prices
     */
    @Query(value = """
            SELECT DATE(timestamp) as trade_date,
                       MAX(aggregated_price) as high_price,
                       MIN(aggregated_price) as low_price,
                       AVG(aggregated_price) as avg_price
                FROM quotes
                WHERE symbol = :symbol\s
                  AND timestamp >= CURRENT_DATE - CAST(:days || ' days' AS INTERVAL)
                GROUP BY trade_date
                ORDER BY trade_date DESC
            """, nativeQuery = true)
    List<DailySummary> findDailySummary(@Param("symbol") String symbol, @Param("days") int days);
}
