package com.trading.cryptotradingsim.cryptotradingsimbe.dto.request;

import com.trading.cryptotradingsim.cryptotradingsimbe.dto.OrderType;

public record OrderRequest(OrderType orderType, String currencyPair, Double amount) {
}