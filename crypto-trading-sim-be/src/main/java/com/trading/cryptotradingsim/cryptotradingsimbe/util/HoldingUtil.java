package com.trading.cryptotradingsim.cryptotradingsimbe.util;

import com.trading.cryptotradingsim.cryptotradingsimbe.dto.entity.HoldingEntity;
import com.trading.cryptotradingsim.cryptotradingsimbe.dto.model.Holding;
import com.trading.cryptotradingsim.cryptotradingsimbe.dto.model.Trade;
import com.trading.cryptotradingsim.cryptotradingsimbe.dto.response.HoldingResponse;

import java.time.OffsetDateTime;

public final class HoldingUtil {

    public static HoldingResponse toResponse(Holding holding) {
        return new HoldingResponse(
                holding.getCryptocurrencySymbol(),
                holding.getQuantity(),
                holding.getAveragePrice(),
                holding.getFiatCurrency()
        );
    }

    public static Holding toModel(HoldingEntity entity) {
        Holding model = new Holding();
        model.setUserId(entity.getUserId());
        model.setCryptocurrencySymbol(entity.getCryptocurrencySymbol());
        model.setQuantity(entity.getQuantity());
        model.setAveragePrice(entity.getAveragePrice());
        model.setUpdatedAt(entity.getUpdatedAt());
        return model;
    }

    public static HoldingEntity toEntity(Holding model) {
        HoldingEntity entity = new HoldingEntity();
        entity.setCryptocurrencySymbol(model.getCryptocurrencySymbol());
        entity.setQuantity(model.getQuantity());
        entity.setAveragePrice(model.getAveragePrice());
        entity.setUpdatedAt(model.getUpdatedAt());
        return entity;
    }

    public static HoldingEntity toEntity(Trade trade) {
        HoldingEntity entity = new HoldingEntity();
        entity.setUserId(trade.getUserId());
        entity.setCryptocurrencySymbol(trade.getCryptocurrencySymbol());
        entity.setQuantity(trade.getQuantity());
        entity.setAveragePrice(trade.getPricePerUnit());
        entity.setFiatCurrency(trade.getFiatCurrency());
        entity.setUpdatedAt(OffsetDateTime.now());
        return entity;
    }
}
