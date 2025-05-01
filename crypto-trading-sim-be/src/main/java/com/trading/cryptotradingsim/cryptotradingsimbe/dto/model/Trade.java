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
public class Trade {
    private String cryptocurrencySymbol;
    private String fiatCurrency;
    private Double quantity;
    private OrderType orderType;
    private Double pricePerUnit;
    private UUID userId;
    private Instant timestamp;
    private Double profitLoss;
}
