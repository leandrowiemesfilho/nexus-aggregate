CREATE TABLE quotes
(
    id               BIGSERIAL PRIMARY KEY,
    symbol           VARCHAR(10)    NOT NULL,
    aggregated_price DECIMAL(19, 4) NOT NULL,
    timestamp        TIMESTAMP      NOT NULL,
    source_prices    JSONB
);

-- Create indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_quotes_symbol ON quotes(symbol);
CREATE INDEX IF NOT EXISTS idx_quotes_timestamp ON quotes(timestamp);
CREATE INDEX IF NOT EXISTS idx_quotes_symbol_timestamp ON quotes(symbol, timestamp);