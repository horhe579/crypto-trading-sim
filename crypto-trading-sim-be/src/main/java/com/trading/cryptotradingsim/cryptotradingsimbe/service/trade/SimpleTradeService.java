package com.trading.cryptotradingsim.cryptotradingsimbe.service.trade;

import com.trading.cryptotradingsim.cryptotradingsimbe.dto.OrderType;
import com.trading.cryptotradingsim.cryptotradingsimbe.dto.entity.TradeEntity;
import com.trading.cryptotradingsim.cryptotradingsimbe.dto.model.Trade;
import com.trading.cryptotradingsim.cryptotradingsimbe.dto.model.User;
import com.trading.cryptotradingsim.cryptotradingsimbe.repository.trade.TradeRepository;
import com.trading.cryptotradingsim.cryptotradingsimbe.service.holding.HoldingService;
import com.trading.cryptotradingsim.cryptotradingsimbe.service.user.UserService;
import com.trading.cryptotradingsim.cryptotradingsimbe.util.TradeUtil;
import org.springframework.scheduling.annotation.Async;

import java.util.UUID;

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
        UUID userId = trade.getUserId();
        Double totalCost = trade.getQuantity() * trade.getPricePerUnit();
        User user = userService.getUser(userId);

        TradeEntity tradeEntity = TradeUtil.toEntity(trade);
        tradeRepository.save(tradeEntity);

        if (trade.getOrderType() == OrderType.BUY) {
            userService.updateUser(new User(
                    user.getId(),
                    user.getBalance() - totalCost,
                    user.getCreatedAt()
            ));
        } else {
            userService.updateUser(new User(
                    user.getId(),
                    user.getBalance() + totalCost,
                    user.getCreatedAt()
            ));
        }

        if (trade.getOrderType() == OrderType.BUY) {
            if (holdingService.hasHolding(userId, trade.getCryptocurrencySymbol())) {
                holdingService.updateHolding(trade);
            } else {
                holdingService.createHolding(trade);
            }
        } else {
            holdingService.updateHolding(trade);
        }
    }
}
