package com.trading.cryptotradingsim.cryptotradingsimbe.service.trade;

import com.trading.cryptotradingsim.cryptotradingsimbe.dto.model.Order;
import com.trading.cryptotradingsim.cryptotradingsimbe.exception.InsufficientFundsException;
import com.trading.cryptotradingsim.cryptotradingsimbe.exception.InvalidPriceException;
import com.trading.cryptotradingsim.cryptotradingsimbe.service.coin.CoinDataService;
import com.trading.cryptotradingsim.cryptotradingsimbe.service.holding.HoldingService;
import com.trading.cryptotradingsim.cryptotradingsimbe.service.user.UserService;
import com.trading.cryptotradingsim.cryptotradingsimbe.util.OrderUtil;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Slf4j
public class AsynchronousOrderService implements OrderService {

    private final CoinDataService coinDataService;
    private final UserService userService;
    private final HoldingService holdingService;
    private final TradeService tradeService;

    public AsynchronousOrderService(CoinDataService coinDataService, UserService userService,
                                    HoldingService holdingService, TradeService tradeService) {
        this.coinDataService = coinDataService;
        this.userService = userService;
        this.holdingService = holdingService;
        this.tradeService = tradeService;
    }

    @Override
    public Order executeBuy(Order buyOrder) {
        double lastPrice = getValidPrice(buyOrder);

        prepareOrder(buyOrder, lastPrice);
        userService.ensureUserExists(buyOrder.getUserId());
        ensureSufficientFunds(buyOrder, lastPrice);
        tradeService.executeTrade(OrderUtil.toTrade(buyOrder));

        return buyOrder;
    }

    @Override
    public Order executeSell(Order sellOrder) {
        // TODO Race condition when 2 threads reach this in a near same time, they both see user does not exist and try and create one,
        // TODO by the time t2 starts with user creation t1 is finished resulting in a JDBC exception as the user alr exists
        prepareOrder(sellOrder, getValidPrice(sellOrder));
        userService.ensureUserExists(sellOrder.getUserId());
        ensureSufficientHolding(sellOrder);
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

    private void ensureSufficientHolding(Order sellOrder) {
        String cryptocurrencySymbol = extractCryptocurrencySymbol(sellOrder);

        if (!holdingService.hasSufficientHolding(sellOrder.getUserId(), cryptocurrencySymbol, sellOrder.getQuantity())) {
            throw new InsufficientFundsException("Insufficient holdings to execute sell order");
        }
    }

    private void ensureSufficientFunds(Order buyOrder, Double currentPrice) {
        double totalCost = buyOrder.getQuantity() * currentPrice;

        if (!userService.hasSufficientFunds(buyOrder.getUserId(), totalCost)) {
            throw new InsufficientFundsException("Insufficient funds to execute buy order");
        }
    }

    private static String extractCryptocurrencySymbol(Order sellOrder) {
        String[] currencyPair = sellOrder.getCurrencyPair().split("/");
        return currencyPair[0];
    }

    private void prepareOrder(Order order, Double pricePerUnit) {
        order.setPricePerUnit(pricePerUnit);
        order.setTimestamp(Instant.now());
    }
}
