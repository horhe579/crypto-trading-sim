package com.trading.cryptotradingsim.cryptotradingsimbe.util;

import com.trading.cryptotradingsim.cryptotradingsimbe.dto.entity.TradeEntity;
import com.trading.cryptotradingsim.cryptotradingsimbe.dto.model.Order;
import com.trading.cryptotradingsim.cryptotradingsimbe.dto.model.Trade;

public class TradeUtil {
    private static final String CURRENCY_PAIR_SEPARATOR = "/";
    private static final String DEFAULT_FIAT_CURRENCY = "USD";

    public static Trade toModel(TradeEntity entity) {
        Trade trade = new Trade();
        trade.setId(entity.getId());
        trade.setCurrencyPair(entity.getCryptocurrencySymbol() + CURRENCY_PAIR_SEPARATOR + entity.getFiatCurrency());
        trade.setQuantity(entity.getQuantity());
        trade.setOrderType(entity.getTradeType());
        trade.setPricePerUnit(entity.getPricePerUnit());
        trade.setUserId(entity.getUserId());
        trade.setTimestamp(entity.getTimestamp());
        trade.setProfitLoss(entity.getProfitLoss());
        return trade;
    }

    public static TradeEntity toEntity(Trade model) {
        String[] currencies = model.getCurrencyPair().split(CURRENCY_PAIR_SEPARATOR);
        TradeEntity entity = new TradeEntity();
        entity.setId(model.getId());
        entity.setUserId(model.getUserId());
        entity.setTradeType(model.getOrderType());
        entity.setCryptocurrencySymbol(currencies[0]);
        entity.setQuantity(model.getQuantity());
        entity.setPricePerUnit(model.getPricePerUnit());
        entity.setFiatCurrency(currencies[1]);
        entity.setProfitLoss(model.getProfitLoss());
        entity.setTimestamp(model.getTimestamp());
        return entity;
    }

    public static TradeEntity toEntity(Order order) {
        String[] currencies = order.getCurrencyPair().split(CURRENCY_PAIR_SEPARATOR);
        TradeEntity entity = new TradeEntity();
        entity.setUserId(order.getUserId());
        entity.setTradeType(order.getOrderType());
        entity.setCryptocurrencySymbol(currencies[0]);
        entity.setQuantity(order.getQuantity());
        entity.setPricePerUnit(order.getPricePerUnit());
        entity.setFiatCurrency(currencies[1]);
        entity.setTimestamp(order.getTimestamp());
        return entity;
    }

    public static Trade toModel(Order order) {
        Trade trade = new Trade();
        trade.setCurrencyPair(order.getCurrencyPair());
        trade.setQuantity(order.getQuantity());
        trade.setOrderType(order.getOrderType());
        trade.setPricePerUnit(order.getPricePerUnit());
        trade.setUserId(order.getUserId());
        trade.setTimestamp(order.getTimestamp());
        return trade;
    }
} 