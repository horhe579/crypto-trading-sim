package com.trading.cryptotradingsim.cryptotradingsimbe.service.trade;

import com.trading.cryptotradingsim.cryptotradingsimbe.dto.model.Order;
import com.trading.cryptotradingsim.cryptotradingsimbe.exception.InsufficientFundsException;
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
        Double currentPrice = coinDataService.getLastPrice(buyOrder.getCurrencyPair());

        // Not really getting anything rename to ensureExistsUser
        userService.getOrCreateUser(buyOrder.getUserId());

        Instant now = Instant.now();
        buyOrder.setTimestamp(now);
        buyOrder.setPricePerUnit(currentPrice);

        log.error("Quantity: {}", buyOrder.getQuantity());
        double totalCost = buyOrder.getQuantity() * currentPrice;
        if (!userService.hasSufficientFunds(buyOrder.getUserId(), totalCost)) {
            throw new InsufficientFundsException("Insufficient funds to execute buy order");
        }

        new Thread(() -> {
            try {
                tradeService.executeTrade(OrderUtil.toTrade(buyOrder));
            } catch (Exception e) {
                log.error("Error executing buy trade: {}", e.getMessage(), e);
            }
        }).start();

        return buyOrder;
    }

    @Override
    public Order executeSell(Order sellOrder) {
        Double currentPrice = coinDataService.getLastPrice(sellOrder.getCurrencyPair());

        // Not really getting anything rename to ensureExistsUser
        userService.getOrCreateUser(sellOrder.getUserId());

        Instant now = Instant.now();
        sellOrder.setTimestamp(now);
        sellOrder.setPricePerUnit(currentPrice);

        String[] currencyPair = sellOrder.getCurrencyPair().split("/");
        String cryptocurrencySymbol = currencyPair[0];
        if (!holdingService.hasSufficientHolding(sellOrder.getUserId(), cryptocurrencySymbol, sellOrder.getQuantity())) {
            throw new InsufficientFundsException("Insufficient holdings to execute sell order");
        }

        new Thread(() -> {
            try {
                tradeService.executeTrade(OrderUtil.toTrade(sellOrder));
            } catch (Exception e) {
                log.error("Error executing sell trade: {}", e.getMessage(), e);
            }
        }).start();

        return sellOrder;
    }
}
