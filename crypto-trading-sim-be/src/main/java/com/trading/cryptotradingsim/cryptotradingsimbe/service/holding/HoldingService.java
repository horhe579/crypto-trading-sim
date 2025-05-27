package com.trading.cryptotradingsim.cryptotradingsimbe.service.holding;

import com.trading.cryptotradingsim.cryptotradingsimbe.dto.model.Holding;
import com.trading.cryptotradingsim.cryptotradingsimbe.dto.model.Trade;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HoldingService {

    Holding createHolding(Trade trade);

    Holding updateHolding(Trade trade);

    boolean hasHolding(UUID userId, String cryptocurrencySymbol);

    List<Holding> getHoldings(UUID userId);

    Optional<Holding> getHolding(UUID userId, String cryptocurrencySymbol);

    boolean hasSufficientHolding(UUID userId, String cryptocurrencySymbol, double quantity);
} 