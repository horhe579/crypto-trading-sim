package com.trading.cryptotradingsim.cryptotradingsimbe.dto.request;

import com.trading.cryptotradingsim.cryptotradingsimbe.dto.OrderType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record OrderRequest(
        @NotNull
        OrderType orderType,

        @NotNull
        @Pattern(regexp = "^[A-Z]{3,5}/USD$", message = "currencyPair must be in the format XXX/USD or XXXX/USD")
        String currencyPair,

        @NotNull
        @DecimalMin(value = "0.00000001", message = "Quantity must be greater than 0")
        Double quantity
) {
}
