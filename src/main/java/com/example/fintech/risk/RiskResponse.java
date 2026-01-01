//RiskResponse
//riskScore
//decision

package com.example.fintech.risk;

public class RiskResponse {

    private int riskScore;
    private String decision;

    public RiskResponse() {}

    public RiskResponse(int riskScore, String decision) {
        this.riskScore = riskScore;
        this.decision = decision;
    }

    public int getRiskScore() {
        return riskScore;
    }

    public String getDecision() {
        return decision;
    }
}

