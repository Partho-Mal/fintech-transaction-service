//TransactionController
//POST /api/v1/transactions
//call TransactionService
//return response
package com.example.fintech.controller;

import com.example.fintech.domain.Transaction;
import com.example.fintech.service.TransactionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService){
        this.transactionService = transactionService;
    }

    @PostMapping
    public Map<String, Object> createTransaction(
            @RequestBody Map<String, String> request
    ) {
        Transaction transaction = transactionService.createTransaction(
          request.get("idempotencyKey"),
          new BigDecimal(request.get("amount")),
          request.get("currency")
        );

        return Map.of(
                "transactionId", transaction.getId(),
                "status", transaction.getStatus()
        );
    }
}
