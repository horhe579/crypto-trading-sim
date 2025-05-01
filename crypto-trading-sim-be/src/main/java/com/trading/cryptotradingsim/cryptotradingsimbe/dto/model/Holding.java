package com.trading.cryptotradingsim.cryptotradingsimbe.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Holding {
    private UUID userId;
    private String cryptocurrencySymbol;
    private Double quantity;
    private Double averagePrice;
    private String fiatCurrency;
    private OffsetDateTime updatedAt;
}
