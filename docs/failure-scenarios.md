# failure-scenarios.md

## Duplicate Request

```mermaid
flowchart TD
    Request --> IdempotencyCheck
    IdempotencyCheck -->|Exists| Reject
    IdempotencyCheck -->|New| Process
```

* Duplicate idempotency key
* Deterministic rejection
* No double charge

---

## Concurrent Update

```mermaid
flowchart TD
    T1[TX1] --> DB
    T2[TX2] --> DB
    DB -->|Version conflict| FailTX2
```

* Optimistic locking detects conflict
* One succeeds, one fails cleanly

---

## Kafka Consumer Failure

```mermaid
flowchart TD
    Event --> Consumer
    Consumer -->|Exception| Retry
    Retry -->|Exhausted| DLQ
```

* No message loss
* DLQ preserves failed events

---

## Risk Service Timeout

```mermaid
flowchart TD
    Consumer --> Risk
    Risk -->|Timeout| Fallback
```

* Core transaction unaffected
* Safe default applied

---
