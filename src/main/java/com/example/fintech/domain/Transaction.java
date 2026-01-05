//Transaction
//id
//idempotencyKey
//money
//status
//version
//createdAt
//constructor
//getters

package com.example.fintech.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Entity // 1. JPA
@Table(name = "transactions")
public class Transaction {

    // Getters
    @Getter
    @Id // 2. Primary Key
    @GeneratedValue(strategy = GenerationType.UUID) // Auto-generate UUID
    private UUID id;

    @Getter
    @Column(nullable = false, unique = true)
    private String idempotencyKey;

    @Getter
    @Embedded // 3. Flattens 'Money' fields (amount, currency) into this table
    private Money money;

    @Getter
    @Enumerated(EnumType.STRING) // 4. Stores "CREATED" as text in DB
    private TransactionStatus status;

    @Version // 5. Optimistic Locking
    private Long version;

    private Instant createdAt;

    // JPA requires a no-arg constructor
    protected Transaction() {}

    public Transaction(String idempotencyKey, Money money) {
        this.idempotencyKey = idempotencyKey;
        this.money = money;
        this.status = TransactionStatus.CREATED;
        this.createdAt = Instant.now();
    }

}