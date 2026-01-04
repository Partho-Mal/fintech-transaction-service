# Fintech Transaction Service

A production-style Spring Boot backend implementing **money-safe transaction processing** with idempotency, concurrency safety, event-driven workflows, security, and resilience against downstream failures.

This project is built incrementally to mirror how real fintech backends are designed, hardened, secured, and operated in production environments.

---

## Architecture Overview

* Layered architecture with strict separation of concerns
* Database is the source of truth for all money-related operations
* Idempotency and optimistic locking ensure correctness under concurrency
* Asynchronous workflows are handled via Kafka with retry and Dead Letter Queue (DLQ)
* External dependencies are integrated with timeouts and fallbacks to prevent cascading failures
* Security and observability are treated as first-class concerns

---

## Day 1 – Core Domain Correctness

### Implemented

* Transaction domain model with immutable idempotency key
* Money value object (amount + currency)
* Idempotent transaction creation enforced at service layer
* Database-backed uniqueness constraint for idempotency
* Clean controller → service → repository separation
* Global exception handling with deterministic API responses

### Explicitly Not Implemented

* Kafka or async processing
* Redis or caching
* Filters or interceptors
* DTOs and validation annotations

**Goal:** Money-safe transaction creation with deterministic behavior.

---

## Day 2 – Concurrency & Failure Safety

### Implemented

* Optimistic locking using `@Version`
* Safe handling of concurrent updates
* Deterministic failure on write conflicts
* Clean mapping of concurrency failures to API responses

**Guarantee:**
No double processing. No silent overwrites. Ever.

---

## Day 3 – Event-Driven Architecture with Kafka + DLQ

### Implemented

* Event-driven transaction workflow using Kafka
* Events published **only after database commit**
* Kafka consumer with failure isolation
* Retry-safe processing with Dead Letter Queue (DLQ)
* At-least-once delivery handled safely via idempotency

**Guarantee:**
Asynchronous processing without message loss or money inconsistency.

---

## Day 4 – Redis, Risk Service & Resilience

### Implemented

* Redis-backed rate limiting to protect APIs from abuse
* External risk scoring service integration (Python-based)
* Mandatory timeouts for all external calls
* Explicit fallback behavior on downstream failure
* Risk evaluation performed asynchronously (non-blocking)

**Guarantee:**
Downstream failures do not block transaction processing.

---

## Day 5 – Security & Observability

### Implemented

* JWT-based authentication
* Role-based access control (USER, ADMIN)
* Authentication enforced via security filter chain
* No authentication logic inside controllers or services
* Structured JSON logging using Logback
* Production-readable logs suitable for aggregation and tracing

**Guarantee:**
Service is observable and secured for internal usage.

---

## Day 6 – Containerization & System Design Documentation

### Implemented

* Dockerized application setup for reproducible local and server deployments
* Docker Compose configuration for running the full stack locally
* System design documentation covering architecture, request flow, failure handling, and scaling strategy

**Guarantee:**
System behavior is reproducible, documented, and explainable end-to-end.

---

## API

### Create Transaction

`POST /api/v1/transactions`

#### Request

```json
{
  "idempotencyKey": "abc-123",
  "amount": "100.00",
  "currency": "INR"
}
```

#### Response

```json
{
  "transactionId": "uuid",
  "status": "CREATED"
}
```

---

## Engineering Principles Followed

* Database-first correctness for financial data
* Explicit handling of concurrency and idempotency
* Event-driven design with clear failure boundaries
* Fail-fast behavior over hidden retries
* Security and observability built into the core system
* Incremental hardening instead of premature optimization

---

## Current Status

Implemented up to **Day 6**:

* Core domain correctness
* Concurrency safety
* Event-driven async workflows
* Resilience against abuse and downstream failures
* JWT security and structured observability
* Containerized setup and system design documentation

---

## Tech Stack

* Java 21
* Spring Boot
* PostgreSQL
* Kafka
* Redis
* Docker

---

## License

MIT
