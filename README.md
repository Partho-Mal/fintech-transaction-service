# Fintech Transaction Service

A production-style Spring Boot backend implementing **money-safe transaction processing** with idempotency, concurrency safety, event-driven workflows, and resilience against downstream failures.

This project is built incrementally to mirror how real fintech backends are designed, hardened, and scaled.

---

## Architecture Overview

- Layered architecture with strict separation of concerns
- Database is the source of truth for all money-related operations
- Idempotency and optimistic locking ensure correctness under concurrency
- Asynchronous workflows are handled via Kafka with retry and DLQ
- External dependencies are integrated with timeouts and fallbacks to prevent cascading failures

---

## Day 1 – Core Domain Correctness

### Implemented
- Transaction domain model with immutable idempotency key
- Money value object (amount + currency)
- Idempotent transaction creation enforced at service layer
- Database-backed uniqueness constraint for idempotency
- Clean controller → service → repository separation
- Global exception handling with deterministic API responses

### Explicitly Not Implemented
- Kafka or async processing
- Redis or caching
- Filters or interceptors
- DTOs and validation annotations

**Goal:** Money-safe transaction creation with deterministic behavior.

---

## Day 2 – Concurrency & Failure Safety

### Implemented
- Optimistic locking using `@Version`
- Safe handling of concurrent updates
- Deterministic failure on write conflicts
- Clean mapping of concurrency failures to API responses

**Guarantee:**  
No double processing. No silent overwrites. Ever.

---

## Day 3 – Event-Driven Architecture with Kafka + DLQ

### Implemented
- Event-driven transaction workflow using Kafka
- Events published **only after database commit**
- Kafka consumer with failure isolation
- Retry-safe processing with Dead Letter Queue (DLQ)
- At-least-once delivery handled safely via idempotency

**Guarantee:**  
Asynchronous processing without message loss or money inconsistency.

---

## Day 4 – Redis, Risk Service & Resilience

### Implemented
- Redis-backed rate limiting to protect APIs from abuse
- External risk scoring service integration (Python-based)
- Mandatory timeouts for all external calls
- Explicit fallback behavior on downstream failure
- Risk evaluation performed asynchronously (non-blocking)

**Guarantee:**  
Downstream failures do not block transaction processing.

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
