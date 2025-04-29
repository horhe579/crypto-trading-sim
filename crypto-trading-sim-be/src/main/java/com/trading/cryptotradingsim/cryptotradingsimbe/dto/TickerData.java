package com.trading.cryptotradingsim.cryptotradingsimbe.dto;

public record TickerData(
        String currencyPair,
        double bid,
        double bid_qty,
        double ask,
        double ask_qty,
        double last,
        double volume,
        double vwap,
        double low,
        double high,
        double change,
        double change_pct
) {
}