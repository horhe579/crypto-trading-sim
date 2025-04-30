package com.trading.cryptotradingsim.cryptotradingsimbe.repository.trade;

import com.trading.cryptotradingsim.cryptotradingsimbe.dto.model.Trade;
import com.trading.cryptotradingsim.cryptotradingsimbe.repository.SimpleRepository;

import java.util.UUID;

public interface TradeRepository extends SimpleRepository<Trade, UUID> {
}
