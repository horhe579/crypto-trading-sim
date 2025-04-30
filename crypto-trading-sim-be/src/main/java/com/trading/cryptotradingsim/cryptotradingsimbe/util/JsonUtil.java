package com.trading.cryptotradingsim.cryptotradingsimbe.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import static com.trading.cryptotradingsim.cryptotradingsimbe.util.ConstantHolder.OBJECT_MAPPER;

public final class JsonUtil {

    public static JsonNode fromMessage(String message) throws JsonProcessingException {
        return OBJECT_MAPPER.readTree(message);
    }
}
