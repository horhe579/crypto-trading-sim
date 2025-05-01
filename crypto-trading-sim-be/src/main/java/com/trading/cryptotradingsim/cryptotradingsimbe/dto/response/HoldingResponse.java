package com.trading.cryptotradingsim.cryptotradingsimbe.dto.response;

public record HoldingResponse(
        String cryptocurrencySymbol,
        double quantity,
        double averagePrice,
        String fiatCurrency
) {
}