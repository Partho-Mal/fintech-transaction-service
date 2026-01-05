# scaling-strategy.md

## Stateless Scaling

```mermaid
flowchart LR
    LB --> App1
    LB --> App2
    LB --> AppN
```

* Horizontal scaling
* No session state

---

## Database Scaling

```mermaid
flowchart LR
    App --> PrimaryDB
    PrimaryDB --> ReadReplica
```

* Writes to primary
* Reads scale independently

---

## Kafka Scaling

```mermaid
flowchart LR
    Topic --> P1
    Topic --> P2
    Topic --> P3
```

* Partitions increase throughput
* Consumers scale horizontally

---