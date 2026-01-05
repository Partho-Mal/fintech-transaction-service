//TransactionController
//POST /api/v1/transactions
//call TransactionService
//return response
package com.example.fintech.controller;

import com.example.fintech.domain.Transaction;
import com.example.fintech.dto.TransactionRequest; // Import the DTO
import com.example.fintech.service.TransactionService;
import com.example.fintech.ratelimit.RateLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final RateLimiter rateLimiter;

    public TransactionController(TransactionService transactionService, RateLimiter rateLimiter) {
        this.transactionService = transactionService;
        this.rateLimiter = rateLimiter;
    }

    @PostMapping
    public Map<String, Object> createTransaction(@RequestBody TransactionRequest request) {
        // 1. Rate Limiting (Fast fail using Redis)
        // Use the key from the DTO record
        String rateLimitKey = "tx:" + request.idempotencyKey();

        if (!rateLimiter.isAllowed(rateLimitKey)) {
            throw new ResponseStatusException(
                    HttpStatus.TOO_MANY_REQUESTS,
                    "Rate limit exceeded. Please retry later."
            );
        }

        // 2. Call Service (Safe inputs from DTO)
        Transaction transaction = transactionService.createTransaction(
                request.idempotencyKey(),
                request.amount(),
                request.currency()
        );

        // 3. Return Response
        return Map.of(
                "transactionId", transaction.getId(),
                "status", transaction.getStatus()
        );
    }
}


//package com.example.fintech.controller;
//
//import com.example.fintech.domain.Transaction;
//import com.example.fintech.service.TransactionService;
//import com.example.fintech.ratelimit.RateLimiter;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.math.BigDecimal;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/v1/transactions")
//public class TransactionController {
//
//    private final TransactionService transactionService;
//    private final RateLimiter rateLimiter;
//
//    public TransactionController(
//            TransactionService transactionService,
//            RateLimiter rateLimiter
//    ) {
//        this.transactionService = transactionService;
//        this.rateLimiter = rateLimiter;
//    }
//
//    @PostMapping
//    public Map<String, Object> createTransaction(
//            @RequestBody Map<String, String> request
//    ) {
//        String rateLimitKey = "tx:" + request.get("idempotencyKey");
//
//        if (!rateLimiter.isAllowed(rateLimitKey)) {
//            throw new ResponseStatusException(
//                    HttpStatus.TOO_MANY_REQUESTS,
//                    "Too many requests"
//            );
//        }
//
//        Transaction transaction = transactionService.createTransaction(
//                request.get("idempotencyKey"),
//                new BigDecimal(request.get("amount")),
//                request.get("currency")
//        );
//
//        return Map.of(
//                "transactionId", transaction.getId(),
//                "status", transaction.getStatus()
//        );
//    }
//}
