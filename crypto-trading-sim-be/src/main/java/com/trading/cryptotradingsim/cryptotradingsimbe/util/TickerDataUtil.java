package com.trading.cryptotradingsim.cryptotradingsimbe.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.trading.cryptotradingsim.cryptotradingsimbe.dto.TickerData;

import java.util.ArrayList;
import java.util.List;

public final class TickerDataUtil {

    public static List<TickerData> parseTickerData(JsonNode dataNode) {
        List<TickerData> tickerData = new ArrayList<>();

        for (JsonNode dataItem : dataNode) {
            tickerData.add(parseTickerDataInternal(dataItem));
        }

        return tickerData;
    }

    private static TickerData parseTickerDataInternal(JsonNode dataItem) {
        String currencyPair = dataItem.path("symbol").asText();
        double bid = dataItem.path("bid").asDouble();
        double bidQty = dataItem.path("bid_qty").asDouble();
        double ask = dataItem.path("ask").asDouble();
        double askQty = dataItem.path("ask_qty").asDouble();
        double last = dataItem.path("last").asDouble();
        double volume = dataItem.path("volume").asDouble();
        double vwap = dataItem.path("vwap").asDouble();
        double low = dataItem.path("low").asDouble();
        double high = dataItem.path("high").asDouble();
        double change = dataItem.path("change").asDouble();
        double changePct = dataItem.path("change_pct").asDouble();

        return new TickerData(currencyPair, bid, bidQty, ask, askQty, last, volume, vwap, low, high, change, changePct);
    }
}
