//Retry count
//No infinite loops

package com.example.fintech.service;

import org.springframework.stereotype.Service;

@Service
public class RetryPolicyService {

    private static final int MAX_RETRIES = 1;

    public boolean shouldRetry(int attempt) {
        return attempt < MAX_RETRIES;
    }
}
