package com.trading.cryptotradingsim.cryptotradingsimbe.service.trade;

import com.trading.cryptotradingsim.cryptotradingsimbe.dto.entity.TradeEntity;
import com.trading.cryptotradingsim.cryptotradingsimbe.dto.model.Trade;
import com.trading.cryptotradingsim.cryptotradingsimbe.dto.model.User;
import com.trading.cryptotradingsim.cryptotradingsimbe.repository.trade.TradeRepository;
import com.trading.cryptotradingsim.cryptotradingsimbe.service.holding.HoldingService;
import com.trading.cryptotradingsim.cryptotradingsimbe.service.user.UserService;
import com.trading.cryptotradingsim.cryptotradingsimbe.util.TradeUtil;
import org.springframework.scheduling.annotation.Async;

public class SimpleTradeService implements TradeService {

    private final TradeRepository tradeRepository;
    private final UserService userService;
    private final HoldingService holdingService;

    public SimpleTradeService(TradeRepository tradeRepository, UserService userService, HoldingService holdingService) {
        this.tradeRepository = tradeRepository;
        this.userService = userService;
        this.holdingService = holdingService;
    }

    // TODO Wrap in a transaction(if called within spring managed context) or send to a MQ
    @Async
    @Override
    public void executeTrade(Trade trade) {
        saveTrade(trade);
        updateUserBalance(
                trade,
                userService.getUser(trade.getUserId()),
                trade.getQuantity() * trade.getPricePerUnit());
        updateHolding(trade);
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
