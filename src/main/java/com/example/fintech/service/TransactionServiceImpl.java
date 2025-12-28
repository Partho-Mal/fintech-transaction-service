//TransactionServiceImpl
//implements TransactionService
//has TransactionRepository
//@Transactional createTransaction
//check idempotency
//create Money
//create Transaction
//save and return
package com.example.fintech.service;

import com.example.fintech.domain.Money;
import com.example.fintech.domain.Transaction;
import com.example.fintech.exception.IdempotencyViolationException;
import com.example.fintech.repository.TransactionRepository;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;

public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;

    public TransactionServiceImpl(TransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Transaction createTransaction(
            String idempotencyKey,
            BigDecimal amount,
            String currency
    ) {
        repository.findByIdempotencyKey(idempotencyKey)
                .ifPresent(tx -> {
                    throw new IdempotencyViolationException(
                            "Duplicate transaction request"
                    );
                });

    Money money = new Money(amount, currency);
    Transaction transaction = new Transaction(idempotencyKey, money);

    return repository.save(transaction);
    }
}

