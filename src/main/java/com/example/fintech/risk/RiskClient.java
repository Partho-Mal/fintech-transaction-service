//call python service
//timeout
//fallback

package com.example.fintech.risk;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import java.time.Duration;

@Component
public class RiskClient {

    private final RestTemplate restTemplate;
    private final RiskFallbackHandler fallbackHandler;

    public RiskClient(RestTemplate restTemplate,
                      RiskFallbackHandler fallbackHandler) {
        this.restTemplate = restTemplate;
        this.fallbackHandler = fallbackHandler;
    }

    public RiskResponse assessRisk(String transactionId) {
        try {
            return restTemplate.getForObject(
                    "http://localhost:8000/risk/" + transactionId,
                    RiskResponse.class
            );
        } catch (RestClientException ex) {
            return fallbackHandler.fallback();
        }
    }
}
