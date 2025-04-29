package com.trading.cryptotradingsim.cryptotradingsimbe.dto;

import java.util.List;

public record KrakenSubscribeMessage(String method, Params params) {

    public KrakenSubscribeMessage(List<String> symbols) {
        this("subscribe", new Params("ticker", symbols));
    }

    public record Params(String channel, List<String> symbol) {}
}
