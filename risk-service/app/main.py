# risk-service/app/main.py

from fastapi import FastAPI
import random

app = FastAPI()

@app.get("/risk/{transaction_id}")
def assess_risk(transaction_id: str):
    score = random.randint(1, 100)

    if score < 30:
        decision = "APPROVE"
    elif score < 70:
        decision = "REVIEW"
    else:
        decision = "REJECT"

    return {
        "riskScore": score,
        "decision": decision
    }
