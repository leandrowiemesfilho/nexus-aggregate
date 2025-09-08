# Nexus Aggregate 📈

A high-performance financial data aggregation gateway built with Java 21, Spring Boot 3.2, and modern Java features.
This service aggregates real-time stock data from multiple external APIs, provides caching, persistence, and real-time
streaming capabilities.

## 🚀 Features

- **Real-time Data Aggregation**: Fetches stock data from multiple sources concurrently
- **Java 21 Virtual Threads**: Efficiently handles thousands of concurrent blocking I/O operations
- **Structured Concurrency (Preview)**: Advanced concurrency management with JEP 453
- **Redis Caching**: Implements cache-aside pattern with TTL for rate limit protection
- **PostgreSQL Persistence**: Stores historical data for analytics and reporting
- **Server-Sent Events (SSE)**: Real-time streaming of stock quotes
- **Resilience4j Integration**: Circuit breakers and rate limiting for fault tolerance
- **Dockerized Infrastructure**: Easy deployment with Docker Compose

## 🛠️ Technology Stack

- **Java 21** with Preview Features enabled
- **Spring Boot 3.2**
- **Spring Data JPA** (PostgreSQL)
- **Spring Data Redis**
- **Resilience4j** for fault tolerance
- **Docker & Docker Compose**
- **PostgreSQL 15** with JSONB support
- **Redis 7** for caching
- **Gradle** with Kotlin DSL

## 📋 Prerequisites

- Java 21 JDK
- Docker and Docker Compose
- Gradle 7.6+
- API keys for:
    - [Alpha Vantage](https://www.alphavantage.co/support/#api-key)
    - [Finnhub](https://finnhub.io/docs/api#authentication)

## 🏁 Quick Start

### 1. Clone and Configure

```bash
git clone <repository-url>
cd nexus-aggregate
```

Set your API keys as environment variables:

```bash
export ALPHA_VANTAGE_API_KEY=your_alpha_vantage_key
export FINNHUB_API_KEY=your_finnhub_key
```

You can also configure it in a `.env` file:
```env
ALPHA_VANTAGE_API_KEY=your_actual_alpha_vantage_key_here
FINNHUB_API_KEY=your_actual_finnhub_key_here
```

#### 🔑 How to get your API keys
Alpha advantage and Finnhub provides a free trial for their API consumption

### 2. Start Infrastructure

```bash
docker-compose up -d
```

This will start:

- PostgreSQL on port 5432
- Redis on port 6379

### 3. Build and Run application

```bash
./gradlew build
java -Dspring.threads.virtual.enabled=true --enable-preview -jar build/libs/nexus-aggregate-0.0.1-SNAPSHOT.jar
```

Or else, you can run with Gradle directly

```bash
./gradlew bootRun -Dspring.threads.virtual.enabled=true --args='--enable-preview'
```

The application will start on port 8080.

## 📊 API Endpoints

### REST API

| Endpoint                                     | Method  | Description                       |
|:---------------------------------------------|:--------|:----------------------------------|
| `/api/v1/quote/{ticker}`                     | **GET** | Get aggregated quote for a symbol |

### Streaming API

| Endpoint                  | Method  | Description                    |
|:--------------------------|:--------|:-------------------------------|
| `/api/v1/stream/{ticker}` | **GET** | SSE stream of real-time quotes |

### Management Endpoints

| Endpoint                    | Method  | Description              |
|:----------------------------|:--------|:-------------------------|
| `/actuator/health`          | **GET** | Application health check |
| `/actuator/circuitbreakers` | **GET** | Circuit breaker status   |

## 🎯 Example Usage
### Get a stock quote
```curl
curl http://localhost:8080/api/v1/quote/AAPL
```

**Response**
```json
{
  "symbol": "AAPL",
  "lastPrice": 175.50,
  "sources": {
    "alphaVantage": 175.12,
    "finnhub": 175.88
  }
}
```

### Stream real-time quotes
```curl
curl http://localhost:8080/api/v1/stream/AAPL
```

### Get historical summary
```curl
curl http://localhost:8080/api/v1/stream/AAPL
```

## ⚙️ Configuration
The application configuration is managed via `application.yml`

## 🧪 Testing
Run the test suite:
```bash
./gradlew test
```

Run integration tests (requires Docker):
```bash
./gradlew integrationTest
```

## 🐳 Docker Deployment
### Build the Application Image
```bash
docker build -t nexus-aggregate .
```

## 📈 Performance Features
### Virtual Threads
The application uses Java 21 virtual threads for all blocking I/O operations, enabling high concurrency with minimal resource usage.

### Cache Strategy
- **Redis cache:** 1-minute TTL for latest quotes
- **Cache-aside pattern:** Reduces external API calls
- **Circuit breakers:** Prevents cascade failures

### Structured Concurrency
Uses JEP 453 (Preview) for reliable management of concurrent external API calls.

## 🗄️ Database Schema
The application uses PostgreSQL with the following main table:
```sql
CREATE TABLE quotes (
    id BIGSERIAL PRIMARY KEY,
    symbol VARCHAR(10) NOT NULL,
    aggregated_price DECIMAL(19,4) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    source_prices JSONB
);
```

