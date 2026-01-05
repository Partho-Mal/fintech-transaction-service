import http from "k6/http";
import { check, sleep } from "k6";

  /*
  How to run:
  k6 run concurrency-test.js
  */
export let options = {
  // High concurrency, short duration
  vus: 50,
  duration: "10s",
};

export default function () {
  /*
  STRATEGY: The "Hammer" Pattern

  Every single VU targets the EXACT SAME 'currency' or 'account' (simulated context)
  but sends UNIQUE idempotency keys.

  This forces the database to lock the same rows/entities repeatedly,
  triggering your @Version Optimistic Locking logic.
  */

  // Unique key ensures it's a valid NEW request
  const uniqueKey = `hammer-${__VU}-${__ITER}-${Date.now()}`;

  const payload = JSON.stringify({
    idempotencyKey: uniqueKey,
    amount: "1.00",
    currency: "USD" // Everyone hitting the 'USD' ledger/account
  });

  const params = { headers: { "Content-Type": "application/json" } };

  const res = http.post("http://localhost:8080/api/v1/transactions", payload, params);

  /*
  VALIDATION:
  - 200: Update successful (Version N -> N+1)
  - 409: Conflict (Optimistic Lock Failure - "Someone else updated it first")

  FAIL if you see 500s (System crash) or DB deadlocks.
  */
  check(res, {
    "Status is 200 or 409 (Locking works)": (r) => r.status === 200 || r.status === 409,
    "Response time < 500ms": (r) => r.timings.duration < 500
  });
}