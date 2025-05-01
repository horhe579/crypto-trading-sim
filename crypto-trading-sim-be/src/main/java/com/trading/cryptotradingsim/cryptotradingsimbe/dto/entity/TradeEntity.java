package com.trading.cryptotradingsim.cryptotradingsimbe.dto.entity;

import com.trading.cryptotradingsim.cryptotradingsimbe.dto.OrderType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradeEntity {
    private UUID id;
    private UUID userId;
    private OrderType tradeType;
    private String cryptocurrencySymbol;
    private Double amount;
    private Double pricePerUnit;
    private String fiatCurrency;
    private Double profitLoss;
    private Instant timestamp;
} 