package com.trading.cryptotradingsim.cryptotradingsimbe.dto.model;

import com.trading.cryptotradingsim.cryptotradingsimbe.dto.OrderType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private String currencyPair;
    private Double quantity;
    private OrderType orderType;
    private Double pricePerUnit;
    private UUID userId;
    private Instant timestamp;
}
