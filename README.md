# Fintech Transaction Service

> **A production-grade Spring Boot backend focused on money safety, concurrency correctness, and failure-resilient asynchronous workflows.**

![Java](https://img.shields.io/badge/Java-21-orange) ![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-green) ![Postgres](https://img.shields.io/badge/PostgreSQL-16-blue) ![Kafka](https://img.shields.io/badge/Kafka-3.7-black) ![Redis](https://img.shields.io/badge/Redis-7-red) ![Docker](https://img.shields.io/badge/Docker-Compose-blue)

This project demonstrates how real-world financial systems are designed, hardened, and operated. It moves beyond basic CRUD to address the core challenges of fintech: **idempotency**, **distributed locking**, **eventual consistency**, and **resilience**.

---

## 🏗 Architecture

The system follows a strict layered architecture with event-driven workflows for non-critical paths.

```mermaid
flowchart LR
    Client -->|HTTP POST| API[API Gateway / Service]
    API -->|Rate Limit| Redis[(Redis)]
    API --> Service[Transaction Service]
    Service -->|ACID Tx| DB[(PostgreSQL)]
    
    subgraph "Critical Path"
    Service
    DB
    end
    
    Service -->|After Commit| Kafka[(Kafka)]
    Kafka --> Consumer[Async Consumer]
    Consumer -->|Score| Risk[Risk Engine (Python)]
    Consumer -->|Retry/Fail| DLQ[(Dead Letter Queue)]
```

---

## 🛡️ Core System Guarantees

This service is built to strictly enforce the following invariants:

1.  **Money Safety:** Database is the single source of truth. Arithmetic is handled via explicit Money Value Objects.
2.  **No Double Spending:** Strict **idempotency** ensures that retrying the same request (due to network timeout) never results in a duplicate transaction.
3.  **Concurrency Control:** **Optimistic locking** (`@Version`) prevents silent overwrites when multiple threads access the same account simultaneously.
4.  **Fault Tolerance:** Downstream failures (Risk Service) utilize **timeouts and fallbacks** to prevent cascading system failures.
5.  **Auditability:** Every state change is logged structurally; asynchronous messages are tracked via correlation IDs.

---

## 🚀 Engineering Journey (Implementation Phases)

This project was built incrementally to simulate the evolution of a production system.

| Phase | Focus Area | Engineering Solution |
| :--- | :--- | :--- |
| **Day 1** | **Domain Correctness** | Implemented immutable Idempotency Keys and constraints. Established the "Database-First" money handling strategy. |
| **Day 2** | **Concurrency** | Added **Optimistic Locking**. The system now rejects conflicting writes deterministically rather than overwriting data silently. |
| **Day 3** | **Event-Driven** | Decoupled workflows using **Kafka**. Implemented the "Transactional Outbox" pattern logic (publish only after commit) and DLQs. |
| **Day 4** | **Resilience** | Integrated **Redis** for Rate Limiting. Added circuit-breaking logic and timeouts for the external Python Risk Service. |
| **Day 5** | **Security** | Implemented **JWT Authentication** and Role-Based Access Control (RBAC) via Spring Security filters. |
| **Day 6** | **Ops & Design** | Full **Dockerization**. wrote comprehensive System Design documentation (Capacity estimation, Failure scenarios). |

---

## 🔌 API Reference

### Create Transaction
`POST /api/v1/transactions`

**Request:**
```json
{
  "idempotencyKey": "unique-uuid-v4-abc-123",
  "amount": "1500.50",
  "currency": "INR",
  "description": "Payment for services"
}
```

**Response:**
```json
{
  "transactionId": "550e8400-e29b-41d4-a716-446655440000",
  "status": "CREATED",
  "timestamp": "2024-01-01T12:00:00Z"
}
```

---

## 🛠️ Tech Stack

* **Core:** Java 21, Spring Boot 3.2
* **Data:** PostgreSQL (Primary), Redis (Cache/Rate Limiting)
* **Messaging:** Apache Kafka
* **Security:** Spring Security, JWT
* **Ops:** Docker, Docker Compose
* **Observability:** Structured JSON Logging (Logback)

---

## 🏃‍♂️ How to Run

The entire stack (DB, Kafka, Zookeeper, Redis, API, Risk Service) is containerized.

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/yourusername/fintech-transaction-service.git](https://github.com/yourusername/fintech-transaction-service.git)
    ```

2.  **Start the infrastructure:**
    ```bash
    docker-compose up -d --build
    ```

3.  **Access the Service:**
    * API: `http://localhost:8080`
    * Database (Port): `5432`

---

## 📚 System Design Documentation

Detailed design decisions are documented in the `/docs` folder. These are written to be **System Design Interview Ready**:

* [Architecture & Request Flow](./docs/architecture.md)
* [Concurrency & Locking Strategy](./docs/concurrency.md)
* [Failure Scenarios & Recovery](./docs/failures.md)
* [Scaling Strategy](./docs/scaling.md)

---

## License

MIT