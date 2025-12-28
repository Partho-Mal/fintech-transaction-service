//TransactionService
//createTransaction
//inputs idempotencyKey amount currency
//returns Transaction
package com.example.fintech.service;

import com.example.fintech.domain.Transaction;

import java.math.BigDecimal;

public interface TransactionService {

    Transaction createTransaction(
            String idempotencyKey,
            BigDecimal amount,
            String currency
    );
}
