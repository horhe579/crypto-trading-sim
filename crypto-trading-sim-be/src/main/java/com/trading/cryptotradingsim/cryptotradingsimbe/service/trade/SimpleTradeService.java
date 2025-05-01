package com.trading.cryptotradingsim.cryptotradingsimbe.service.trade;

import com.trading.cryptotradingsim.cryptotradingsimbe.dto.model.Trade;
import com.trading.cryptotradingsim.cryptotradingsimbe.service.holding.HoldingService;
import com.trading.cryptotradingsim.cryptotradingsimbe.service.user.UserService;

public class SimpleTradeService implements TradeService {

    private UserService userService;
    private HoldingService holdingsService;

    @Override
    public void executeTrade(Trade trade) {

    }
}
