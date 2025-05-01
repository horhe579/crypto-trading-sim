package com.trading.cryptotradingsim.cryptotradingsimbe.util;

import com.trading.cryptotradingsim.cryptotradingsimbe.dto.OrderType;
import com.trading.cryptotradingsim.cryptotradingsimbe.dto.model.Order;
import com.trading.cryptotradingsim.cryptotradingsimbe.dto.request.OrderRequest;
import com.trading.cryptotradingsim.cryptotradingsimbe.dto.response.OrderResponse;

import java.util.UUID;

public final class OrderUtil {

    public static Order toModel(OrderRequest request, UUID userId) {
        Order order = new Order();
        order.setCurrencyPair(request.currencyPair());
        order.setQuantity(request.quantity());
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
}
