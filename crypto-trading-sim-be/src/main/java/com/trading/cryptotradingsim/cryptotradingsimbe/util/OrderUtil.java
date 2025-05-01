package com.trading.cryptotradingsim.cryptotradingsimbe.util;

import com.trading.cryptotradingsim.cryptotradingsimbe.dto.OrderType;
import com.trading.cryptotradingsim.cryptotradingsimbe.dto.model.Order;
import com.trading.cryptotradingsim.cryptotradingsimbe.dto.model.Trade;
import com.trading.cryptotradingsim.cryptotradingsimbe.dto.request.OrderRequest;
import com.trading.cryptotradingsim.cryptotradingsimbe.dto.response.OrderResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public final class OrderUtil {
    private OrderUtil() {
    }

    public static Order toModel(OrderRequest request, UUID userId) {
        Order order = new Order();
        order.setCurrencyPair(request.currencyPair());
        log.error("Request quantity: {}", request.quantity());
        order.setQuantity(request.quantity());
        order.setOrderType(request.orderType());
        order.setUserId(userId);
        return order;
    }

    public static OrderResponse toResponse(Order order) {
        OrderType type = order.getOrderType();
        Double quantity = order.getQuantity();
        Double pricePerUnit = order.getPricePerUnit();
        String cryptoSymbol = order.getCurrencyPair();
        String message = String.format("Order of type %s for %s with quantity %s at price %s", type, cryptoSymbol, quantity, pricePerUnit);
        return new OrderResponse(pricePerUnit, quantity, cryptoSymbol, type, order.getTimestamp());
    }

    public static Trade toTrade(Order order) {
        String[] currencyPair = order.getCurrencyPair().split("/");
        Trade trade = new Trade();
        trade.setCryptocurrencySymbol(currencyPair[0]);
        trade.setFiatCurrency(currencyPair[1]);
        trade.setQuantity(order.getQuantity());
        trade.setOrderType(order.getOrderType());
        trade.setPricePerUnit(order.getPricePerUnit());
        trade.setUserId(order.getUserId());
        trade.setTimestamp(order.getTimestamp());
        trade.setProfitLoss(null);
        return trade;
    }

    public static Order toModel(Trade trade, UUID userId) {
        Order order = new Order();
        order.setCurrencyPair(trade.getCryptocurrencySymbol() + "/" + trade.getFiatCurrency());
        order.setQuantity(trade.getQuantity());
        order.setOrderType(trade.getOrderType());
        order.setPricePerUnit(trade.getPricePerUnit());
        order.setUserId(userId);
        order.setTimestamp(trade.getTimestamp());
        return order;
    }
}
