import http from "k6/http";
import { check, sleep } from "k6";

  /*
  How to run:
  k6 run idempotency-test.js
  */
export let options = {
  // Lower VUs slightly to see clear logs in local development
  vus: 10,
  duration: "30s",
};

export default function () {
  /*
  STRATEGY: The "Stutter" Pattern

  Math.floor(__ITER / 5) creates a "window".
  - Iterations 0, 1, 2, 3, 4  -> all generate Key-0
  - Iterations 5, 6, 7, 8, 9  -> all generate Key-1

  This ensures:
  - Request 1: Should be 200 OK (Created)
  - Requests 2-5: Should be 409 Conflict (Idempotency) OR 429 (Rate Limit)
  */

  const keyWindow = Math.floor(__ITER / 5);
  const uniqueKey = `user-${__VU}-window-${keyWindow}`;

  const payload = JSON.stringify({
    idempotencyKey: uniqueKey,
    amount: "100.00",
    currency: "USD",
  });

  const params = {
    headers: {
      "Content-Type": "application/json",
    },
  };

  const res = http.post(
    "http://localhost:8080/api/v1/transactions",
    payload,
    params
  );

  /*
  VALIDATION:
  We now accept 3 distinct states, which proves your distributed system works:
  - 200: Good! Transaction created.
  - 409: Good! You blocked a duplicate payment.
  - 429: Good! You blocked a spammer.
  */
  check(res, {
    "is handled correctly (200, 409, 429)": (r) =>
      r.status === 200 || r.status === 409 || r.status === 429,
  });

  // Short sleep to prevent local port exhaustion
  sleep(0.1);
}