package com.trading.cryptotradingsim.cryptotradingsimbe.dto.response;

import com.trading.cryptotradingsim.cryptotradingsimbe.dto.OrderType;

import java.time.Instant;

public record OrderResponse(Double pricePerUnit,
                            Double quantity,
                            String cryptoSymbol,
                            OrderType orderType,
                            Instant timestamp
) {
}