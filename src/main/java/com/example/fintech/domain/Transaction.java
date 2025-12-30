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

import jakarta.persistence.Version;

import java.time.Instant;
import java.util.UUID;

public class Transaction {

    private UUID id;
    private String idempotencyKey;
    private Money money;
    private TransactionStatus status;
    @Version
    private Long version;
    private Instant createdAt;

    public Transaction(String idempotencyKey, Money money) {
        this.idempotencyKey = idempotencyKey;
        this.money = money;
        this.status = TransactionStatus.CREATED;
        this.createdAt = Instant.now();
    }

    public UUID getId(){
        return id;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public TransactionStatus getStatus() {
        return status;
    }
}
