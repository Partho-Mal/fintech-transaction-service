import http from "k6/http";
import { check } from "k6";

  /*
  How to run:
  k6 run stress-test.js
  */
export let options = {
  // Ramping pattern to find the breaking point
//  stages: [
//      { duration: "10s", target: 20 },  // Warm up
//      { duration: "30s", target: 50 },  // Moderate Load
//      { duration: "30s", target: 100 }, // Peak Load (Reduced from 500)
//      { duration: "10s", target: 0 },   // Cool down
//    ],
  stages: [
    { duration: "10s", target: 50 },  // Warm up
    { duration: "30s", target: 200 }, // High Load (Targeting ~2000+ RPS)
    { duration: "30s", target: 500 }, // Extreme Load (Targeting ~5000+ RPS)
    { duration: "10s", target: 0 },   // Cool down
  ],
  thresholds: {
    http_req_failed: ['rate<0.01'], // Error rate must be < 1%
    http_req_duration: ['p(95)<200'], // 95% of requests must be faster than 200ms
  },
};

export default function () {
  const uniqueKey = `stress-${__VU}-${__ITER}-${Date.now()}`;

  const payload = JSON.stringify({
    idempotencyKey: uniqueKey,
    amount: "10.00",
    currency: "EUR"
  });

  const params = { headers: { "Content-Type": "application/json" } };

  const res = http.post("http://localhost:8080/api/v1/transactions", payload, params);

  check(res, {
    "is status 200": (r) => r.status === 200,
  });
}