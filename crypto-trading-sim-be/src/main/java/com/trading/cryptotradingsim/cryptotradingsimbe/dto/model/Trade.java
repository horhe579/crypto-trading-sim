package com.trading.cryptotradingsim.cryptotradingsimbe.dto.model;

import com.trading.cryptotradingsim.cryptotradingsimbe.dto.OrderType;

import java.time.Instant;
import java.util.UUID;

public class Trade {
    private String currencyPair;
    private Double amount;
    private OrderType orderType;
    private Double pricePerUnit;
    private UUID userId;
    private Instant timestamp;
    private Double profitLoss;
}
