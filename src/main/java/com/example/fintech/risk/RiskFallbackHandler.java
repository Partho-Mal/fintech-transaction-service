//fallback
//return low risk

package com.example.fintech.risk;

import org.springframework.stereotype.Component;

@Component
public class RiskFallbackHandler {

    public RiskResponse fallback() {
        // conservative default
        return new RiskResponse(50, "REVIEW");
    }
}

