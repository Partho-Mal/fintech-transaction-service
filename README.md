# Fintech Transaction Service

A Spring Boot backend service that implements money-safe transaction creation with strict idempotency guarantees.

## Day 1 – Core Domain Correctness

### What is implemented
- Transaction domain model with immutable idempotency key
- Money value object (amount + currency)
- Idempotent transaction creation enforced at service layer
- Database-backed uniqueness guarantee
- Clean separation of controller, service, and repository layers
- Global exception handling with deterministic API responses

### What is intentionally NOT implemented
- Kafka or async processing
- Redis or caching
- Filters or interceptors
- DTOs and validation annotations

These will be added incrementally in later stages.

### API
`POST /api/v1/transactions`

Example request:
```json
{
  "idempotencyKey": "abc-123",
  "amount": "100.00",
  "currency": "INR"
}
```
### Example response:
```json
{
  "transactionId": "uuid",
  "status": "CREATED"
}
```


```bash
We follow a layered architecture with strict separation of concerns.
Domain models are pure and isolated, transactional logic lives in the service layer, concurrency safety is enforced using optimistic locking and idempotency keys, and async workflows are handled via Kafka with retry and DLQ.
External risk scoring is integrated through a resilient client with timeouts and fallbacks.
```