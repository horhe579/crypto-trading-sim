package com.trading.cryptotradingsim.cryptotradingsimbe.service.trade;

import com.trading.cryptotradingsim.cryptotradingsimbe.dto.model.Order;
import com.trading.cryptotradingsim.cryptotradingsimbe.dto.model.User;
import com.trading.cryptotradingsim.cryptotradingsimbe.service.coin.CoinDataService;
import com.trading.cryptotradingsim.cryptotradingsimbe.service.user.UserService;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.UUID;

@Slf4j
public class SynchronousOrderService implements OrderService {

    private final CoinDataService coinDataService;
    private final UserService userService;


    public SynchronousOrderService(CoinDataService coinDataService, UserService userService) {
        this.coinDataService = coinDataService;
        this.userService = userService;
    }

    @Override
    public Order executeBuy(Order buyOrder) {
        Double currentPrice = coinDataService.getLastPrice(buyOrder.getCurrencyPair());
        Instant now = Instant.now();
        buyOrder.setTimestamp(now);
        buyOrder.setPricePerUnit(currentPrice);
        validateSufficientFunds(buyOrder);

        new Thread(() -> {
            try {
                Thread.sleep(1000);
                UUID userId = buyOrder.getUserId();
                double totalCost = buyOrder.getAmount() * currentPrice;

                User user = userService.getUser(userId);
                User updatedUser = new User(
                        user.getId(),
                        user.getBalance() - totalCost,
                        user.getCreatedAt()
                );
                userService.updateUser(updatedUser);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

        return buyOrder;
    }


    @Override
    public Order executeSell(Order sellOrder) {
        return null;
    }

    private void validateSufficientFunds(Order buyOrder) {
        double totalCost = calculateTotalCost(buyOrder);
        if (!hasSufficientFunds(buyOrder.getUserId(), totalCost)) {
            throw new IllegalArgumentException("Insufficient funds to execute order");
        }
    }

    private double calculateTotalCost(Order order) {
        return order.getAmount() * order.getPricePerUnit();
    }

    private boolean hasSufficientFunds(UUID userId, double amount) {
        User user = userService.getOrCreateUser(userId);
        log.error("User: {}, Amount: {}", user, amount);
        return user.getBalance() >= amount;
    }
}
