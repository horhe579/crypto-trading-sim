package com.trading.cryptotradingsim.cryptotradingsimbe.service.trade;

import com.trading.cryptotradingsim.cryptotradingsimbe.dto.model.Order;
import com.trading.cryptotradingsim.cryptotradingsimbe.exception.InvalidPriceException;
import com.trading.cryptotradingsim.cryptotradingsimbe.service.coin.CoinDataService;
import com.trading.cryptotradingsim.cryptotradingsimbe.util.OrderUtil;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Slf4j
public class AsynchronousOrderService implements OrderService {

    private final CoinDataService coinDataService;
    private final TradeService tradeService;

    public AsynchronousOrderService(CoinDataService coinDataService, TradeService tradeService) {
        this.coinDataService = coinDataService;
        this.tradeService = tradeService;
    }

    @Override
    public Order executeBuy(Order buyOrder) {
        prepareOrder(buyOrder, getValidPrice(buyOrder));
        tradeService.executeTrade(OrderUtil.toTrade(buyOrder));
        return buyOrder;
    }

    @Override
    public Order executeSell(Order sellOrder) {
        prepareOrder(sellOrder, getValidPrice(sellOrder));
        tradeService.executeTrade(OrderUtil.toTrade(sellOrder));
        return sellOrder;
    }

    private double getValidPrice(Order order) {
        double lastPrice = coinDataService.getLastPrice(order.getCurrencyPair());
        if (lastPrice < 0) {
            throw new InvalidPriceException("Received an invalid price from provider for pair: " + order.getCurrencyPair());
        }
        return lastPrice;
    }

    private void prepareOrder(Order order, Double pricePerUnit) {
        order.setPricePerUnit(pricePerUnit);
        order.setTimestamp(Instant.now());
    }
}
