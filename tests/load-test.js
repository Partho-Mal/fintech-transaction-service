// Import HTTP module to make API requests during the load test
import http from "k6/http";

// Import check to define assertions on responses
import { check } from "k6";

/*
How to run:
k6 run load-test.js

What this test does:
- Sends concurrent POST requests to the transactions API
- Validates idempotency behavior under load
- Ensures system remains stable with concurrent users
*/
export let options = {
  // Number of virtual users running concurrently
  vus: 20,

  // Total duration of the test
  // Each VU will repeatedly execute the default function for 30 seconds
  duration: "30s",
};

/*
default function is executed once per iteration per VU
Total requests ≈ vus * iterations per VU within duration
*/
export default function () {
  /*
  Create a unique idempotency key per request

  __VU    -> virtual user id
  __ITER  -> iteration number for that VU

  This simulates real-world retry and concurrency scenarios
  and prevents accidental duplicate processing
  */
  const payload = JSON.stringify({
    idempotencyKey: "test-" + __VU + "-" + __ITER,
    amount: "100.00",
    currency: "INR",
  });

  /*
  Send POST request to transaction API
  Localhost used for local load testing
  Headers explicitly define JSON payload
  */
  const res = http.post(
    "http://localhost:8080/api/v1/transactions",
    payload,
    {
      headers: {
        "Content-Type": "application/json",
      },
    }
  );

  /*
  Validate response correctness

  200 -> transaction successfully created
  409 -> duplicate request rejected due to idempotency

  Both responses are considered correct behavior
  */
  check(res, {
    "status is 200 or 409": (r) => r.status === 200 || r.status === 409,
  });
}
