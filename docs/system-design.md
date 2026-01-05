# system-design.md

## System Design Overview

This document provides a complete system design view of the Fintech Transaction Service. It focuses on **correctness, failure handling, scalability, and cost-aware tradeoffs**, without relying on cloud-vendor specifics.

The goal is to demonstrate how the system behaves under real-world constraints.

---

## High-Level Architecture Diagram

```mermaid
flowchart LR
    Client -->|HTTP| API[Controller]
    API -->|Rate Limit| Redis
    API --> Service
    Service -->|TX| DB[(PostgreSQL)]
    Service -->|After Commit| Kafka[(Kafka Topic)]
    Kafka --> Consumer
    Consumer --> Risk[Risk Service]
    Consumer --> DLQ[(Dead Letter Queue)]
```

---

## Request Flow

### Synchronous Transaction Flow

```mermaid
sequenceDiagram
    participant Client
    participant Controller
    participant Redis
    participant Service
    participant DB

    Client->>Controller: POST /transactions
    Controller->>Redis: rate limit check
    Redis-->>Controller: allowed
    Controller->>Service: createTransaction()
    Service->>DB: insert transaction
    DB-->>Service: commit
    Service-->>Controller: response
    Controller-->>Client: 200 CREATED
```

### Asynchronous Side-Effect Flow

```mermaid
sequenceDiagram
    participant Service
    participant Kafka
    participant Consumer
    participant Risk

    Service->>Kafka: publish event (after commit)
    Kafka->>Consumer: transaction event
    Consumer->>Risk: assess risk (timeout)
    Risk-->>Consumer: response / fallback
```

---

## Failure Scenarios (Failure Table)

| Scenario               | Detection Point         | System Behavior  | Outcome                     |
| ---------------------- | ----------------------- | ---------------- | --------------------------- |
| Duplicate request      | Idempotency key check   | Reject request   | No double charge            |
| Concurrent update      | Optimistic lock failure | Return conflict  | Deterministic failure       |
| DB transaction failure | Commit phase            | Rollback         | No partial state            |
| Kafka consumer crash   | Consumer runtime        | Retry → DLQ      | No message loss             |
| Risk service timeout   | HTTP client             | Fallback applied | Core flow unaffected        |
| Redis unavailable      | Rate limit check        | Fail fast        | Transaction logic preserved |

---

## Concurrency Handling

### Strategy

* Idempotency keys prevent duplicate processing
* Optimistic locking (`@Version`) prevents lost updates
* No pessimistic locking or long-held DB locks

### Rationale

* Optimistic locking scales better under contention
* Conflicts are explicit and observable
* Avoids deadlocks and lock amplification

---

## Scaling Strategy

### Application Layer

```mermaid
flowchart LR
    LB --> App1
    LB --> App2
    LB --> AppN
```

* Stateless services
* Horizontal scaling via additional instances
* No session affinity required

### Database Layer

```mermaid
flowchart LR
    App --> PrimaryDB
    PrimaryDB --> ReadReplica
```

* Writes handled by primary
* Read replicas scale read-heavy workloads
* Indexes on idempotency keys

### Messaging Layer

```mermaid
flowchart LR
    Topic --> P1
    Topic --> P2
    Topic --> P3
```

* Kafka partitions scale consumer throughput
* Consumers scale independently

---

## Cost Constraints (Local-First Design)

### Design Decisions

* No dependency on cloud-managed services
* Entire stack runs locally via Docker
* Environment-variable-based configuration

### Tradeoffs

* Lower infrastructure cost
* Slightly lower peak throughput than managed cloud services
* Greater control over behavior and debugging

### Interview Framing

> The system is designed to be cloud-agnostic. Deployment targets can be swapped without changing core logic, which avoids vendor lock-in and uncontrolled cost growth.

---

## Summary

This system demonstrates a production-grade fintech backend designed with real-world constraints in mind. It prioritizes correctness, failure isolation, and explainability over cloud branding. The architecture is scalable, resilient, cost-aware, and suitable for backend and system design interviews.
