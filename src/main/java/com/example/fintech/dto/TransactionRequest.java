package com.example.fintech.dto;

import java.math.BigDecimal;

// Java 21 Record - Immutable, compact, safe data carrier
public record TransactionRequest(
        String idempotencyKey,
        BigDecimal amount,
        String currency
) {}