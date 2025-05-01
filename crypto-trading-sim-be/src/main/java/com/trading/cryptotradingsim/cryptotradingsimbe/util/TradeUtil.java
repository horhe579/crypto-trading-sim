package com.trading.cryptotradingsim.cryptotradingsimbe.util;

import com.trading.cryptotradingsim.cryptotradingsimbe.dto.entity.TradeEntity;
import com.trading.cryptotradingsim.cryptotradingsimbe.dto.model.Order;
import com.trading.cryptotradingsim.cryptotradingsimbe.dto.model.Trade;

public class TradeUtil {
    private static final String CURRENCY_PAIR_SEPARATOR = "/";

    public static Trade toModel(TradeEntity entity) {
        Trade trade = new Trade();
        trade.setFiatCurrency(entity.getFiatCurrency());
        trade.setCryptocurrencySymbol(entity.getCryptocurrencySymbol());
        trade.setQuantity(entity.getQuantity());
        trade.setOrderType(entity.getTradeType());
        trade.setPricePerUnit(entity.getPricePerUnit());
        trade.setUserId(entity.getUserId());
        trade.setTimestamp(entity.getTimestamp());
        trade.setProfitLoss(entity.getProfitLoss());
        return trade;
    }

    public static TradeEntity toEntity(Trade model) {
        TradeEntity entity = new TradeEntity();
        entity.setUserId(model.getUserId());
        entity.setTradeType(model.getOrderType());
        entity.setCryptocurrencySymbol(model.getCryptocurrencySymbol());
        entity.setQuantity(model.getQuantity());
        entity.setPricePerUnit(model.getPricePerUnit());
        entity.setFiatCurrency(model.getFiatCurrency());
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
        String[] currencies = order.getCurrencyPair().split(CURRENCY_PAIR_SEPARATOR);
        Trade trade = new Trade();
        trade.setCryptocurrencySymbol(currencies[0]);
        trade.setFiatCurrency(currencies[1]);
        trade.setQuantity(order.getQuantity());
        trade.setOrderType(order.getOrderType());
        trade.setPricePerUnit(order.getPricePerUnit());
        trade.setUserId(order.getUserId());
        trade.setTimestamp(order.getTimestamp());
        return trade;
    }
} 