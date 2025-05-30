package com.trading.cryptotradingsim.cryptotradingsimbe.service.trade;

import com.trading.cryptotradingsim.cryptotradingsimbe.dto.OrderType;
import com.trading.cryptotradingsim.cryptotradingsimbe.dto.entity.TradeEntity;
import com.trading.cryptotradingsim.cryptotradingsimbe.dto.model.Trade;
import com.trading.cryptotradingsim.cryptotradingsimbe.dto.model.User;
import com.trading.cryptotradingsim.cryptotradingsimbe.exception.InsufficientFundsException;
import com.trading.cryptotradingsim.cryptotradingsimbe.repository.trade.TradeRepository;
import com.trading.cryptotradingsim.cryptotradingsimbe.service.holding.HoldingService;
import com.trading.cryptotradingsim.cryptotradingsimbe.service.user.UserService;
import com.trading.cryptotradingsim.cryptotradingsimbe.util.TradeUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

public class SimpleTradeService implements TradeService {

    private final TradeRepository tradeRepository;
    private final UserService userService;
    private final HoldingService holdingService;

    public SimpleTradeService(TradeRepository tradeRepository, UserService userService, HoldingService holdingService) {
        this.tradeRepository = tradeRepository;
        this.userService = userService;
        this.holdingService = holdingService;
    }


    // TODO: Logs, Error and success Handling with notifications
    @Async
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public void executeTrade(Trade trade) {
        double totalCost = trade.getQuantity() * trade.getPricePerUnit();
        User user = userService.ensureUserExists(trade.getUserId());
        ensureSufficientFunds(user, totalCost);
        if (trade.getOrderType() == OrderType.SELL) ensureSufficientHolding(trade);
        saveTrade(trade);
        updateUserBalance(trade, user, totalCost);
        updateHolding(trade);
    }

    private void ensureSufficientFunds(User user, double totalCost) {
        if (!userService.hasSufficientFunds(user.getId(), totalCost)) {
            throw new InsufficientFundsException("Insufficient funds to execute buy order");
        }
    }

    private void ensureSufficientHolding(Trade trade) {
        String cryptocurrencySymbol = trade.getCryptocurrencySymbol();
        if (!holdingService.hasSufficientHolding(trade.getUserId(), cryptocurrencySymbol, trade.getQuantity())) {
            throw new InsufficientFundsException("Insufficient holdings to execute sell order");
        }
    }

    private void updateHolding(Trade trade) {
        switch (trade.getOrderType()) {
            case BUY, SELL -> holdingService.updateHolding(trade);
            default -> throw new IllegalArgumentException("Invalid Order Type.");
        }
    }

    private void updateUserBalance(Trade trade, User user, Double totalCost) {
        double newBalance = switch (trade.getOrderType()) {
            case BUY -> user.getBalance() - totalCost;
            case SELL -> user.getBalance() + totalCost;
            case null -> throw new IllegalArgumentException("Invalid Order Type.");
        };

        userService.updateUser(new User(
                user.getId(),
                newBalance,
                user.getCreatedAt()
        ));
    }

    private void saveTrade(Trade trade) {
        TradeEntity tradeEntity = TradeUtil.toEntity(trade);
        tradeRepository.save(tradeEntity);
    }
}
