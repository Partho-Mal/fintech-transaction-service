//Check idempotency key
//Delegate to repository

package com.example.fintech.service;

import com.example.fintech.repository.TransactionRepository;
import org.springframework.stereotype.Service;

@Service
public class IdempotencyService {

    private final TransactionRepository repository;

    public IdempotencyService(TransactionRepository repository) {
        this.repository = repository;
    }

    public boolean exists(String idempotencyKey) {
        return repository.findByIdempotencyKey(idempotencyKey).isPresent();
    }
}

