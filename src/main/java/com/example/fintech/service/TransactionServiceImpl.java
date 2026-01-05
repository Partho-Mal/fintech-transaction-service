package com.example.fintech.service;

import com.example.fintech.domain.Money;
import com.example.fintech.domain.Transaction;
import com.example.fintech.exception.ConcurrencyFailureException;
import com.example.fintech.exception.IdempotencyViolationException;
import com.example.fintech.repository.TransactionRepository;
import jakarta.persistence.OptimisticLockException;
import org.springframework.stereotype.Service; // <--- 1. Add this import
import org.springframework.transaction.annotation.Transactional; // Use Spring's Transactional for better support
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.math.BigDecimal;

@Service // <--- 2. Add this annotation here
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;
    private final ApplicationEventPublisher eventPublisher;

    public TransactionServiceImpl(TransactionRepository repository, ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public Transaction createTransaction(
            String idempotencyKey,
            BigDecimal amount,
            String currency
    ) {
        try {
            // Idempotency Check
            repository.findByIdempotencyKey(idempotencyKey)
                    .ifPresent(tx -> {
                        throw new IdempotencyViolationException(
                                "Duplicate transaction request"
                        );
                    });

            Money money = new Money(amount, currency);
            Transaction transaction = new Transaction(idempotencyKey, money);

            Transaction saved = repository.save(transaction);

            // Publish event for side effects (like Risk Service analysis)
            // eventPublisher.publishEvent(new TransactionCreatedEvent(saved));

            return saved;

        } catch (ObjectOptimisticLockingFailureException | OptimisticLockException ex) {
            throw new ConcurrencyFailureException(
                    "Transaction was modified concurrently. Please retry."
            );
        }
    }
}