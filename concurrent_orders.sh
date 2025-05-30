#!/bin/bash

for i in {1..100}; do
  curl -X POST "http://localhost:5911/api/v1/orders/buy" \
    -H "Content-Type: application/json" \
    -H "X-User-Id: 40aa0023-0e43-4f9c-a403-ecde737ad8e4" \
    -d '{"currencyPair":"BTC/USD","quantity":0.09,"orderType":"BUY"}' &
done

wait
