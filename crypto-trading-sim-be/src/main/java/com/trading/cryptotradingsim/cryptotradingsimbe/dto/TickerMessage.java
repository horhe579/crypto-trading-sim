package com.trading.cryptotradingsim.cryptotradingsimbe.dto;

import java.util.List;

public record TickerMessage(String channel,
                            String type,
                            List<TickerData> data) {
}