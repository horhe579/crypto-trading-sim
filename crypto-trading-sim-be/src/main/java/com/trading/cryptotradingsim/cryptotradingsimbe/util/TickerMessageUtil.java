package com.trading.cryptotradingsim.cryptotradingsimbe.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.trading.cryptotradingsim.cryptotradingsimbe.dto.TickerMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

import static com.trading.cryptotradingsim.cryptotradingsimbe.util.TickerDataUtil.parseTickerData;

@Slf4j
public final class TickerMessageUtil {
    private final static Set<String> tickerTypes = Set.of("snapshot", "update");

    public static TickerMessage parseTickerMessage(JsonNode message) {
        String channel = message.path("channel").asText();
        String type = message.path("type").asText();
        JsonNode data = message.path("data");
        String method = message.path("method").asText();
        validateTickerMessage(channel, type, data, method);
        return new TickerMessage(channel, type, parseTickerData(data));
    }

    private static void validateTickerMessage(String channel, String type, JsonNode data, String method) {
        if (!"ticker".equals(channel)) {
            throw new IllegalArgumentException(String.format("Skipping: %s message", channel));
        }
        if (!tickerTypes.contains(type)) {
            throw new IllegalArgumentException(String.format("Skipping: invalid type: %s. Expected one of %s", type, tickerTypes));
        }
        if (data.isMissingNode() || !data.isArray()) {
            throw new IllegalArgumentException("Skipping: invalid or missing 'data' field");
        }
        if (!method.isEmpty()) {
            throw new IllegalArgumentException("Skipping: subscribe message");
        }
    }

}
