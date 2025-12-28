//TransactionRepository
//extends JpaRepository
//find by idempotencyKey

package com.example.fintech.repository;

import com.example.fintech.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository
        extends JpaRepository<Transaction, UUID> {

    Optional<Transaction> findByIdempotencyKey(String idempotencyKey);

}
