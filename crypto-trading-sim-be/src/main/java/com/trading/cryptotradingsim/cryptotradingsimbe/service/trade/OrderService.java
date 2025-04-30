package com.trading.cryptotradingsim.cryptotradingsimbe.service.trade;

import com.trading.cryptotradingsim.cryptotradingsimbe.dto.model.Order;

public interface OrderService {
    Order executeBuy(Order buyOrder);

    Order executeSell(Order sellOrder);
}
