package com.trading.cryptotradingsim.cryptotradingsimbe.service.holding;

import com.trading.cryptotradingsim.cryptotradingsimbe.dto.entity.HoldingEntity;
import com.trading.cryptotradingsim.cryptotradingsimbe.dto.model.Holding;
import com.trading.cryptotradingsim.cryptotradingsimbe.dto.model.Trade;
import com.trading.cryptotradingsim.cryptotradingsimbe.repository.holding.HoldingRepository;
import com.trading.cryptotradingsim.cryptotradingsimbe.util.HoldingUtil;
import lombok.extern.slf4j.Slf4j;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.trading.cryptotradingsim.cryptotradingsimbe.util.HoldingUtil.toEntity;
import static com.trading.cryptotradingsim.cryptotradingsimbe.util.HoldingUtil.toModel;

@Slf4j
public class SimpleHoldingService implements HoldingService {

    private final HoldingRepository holdingRepository;

    public SimpleHoldingService(HoldingRepository holdingRepository) {
        this.holdingRepository = holdingRepository;
    }

    @Override
    public Holding createHolding(Trade trade) {
        return toModel(initializeHolding(trade));
    }

    // all of the logic is specific to one currency, no conversion is done
    // TODO fix this - race condition
    @Override
    public Holding updateHolding(Trade trade) {
        HoldingEntity entity = getOrCreateHolding(trade);

        double quantityChange = getQuantityChange(trade);
        double newQuantity = calculateNewQuantity(entity, quantityChange);
        double newAveragePrice = calculateNewAveragePrice(entity.getAveragePrice(), entity.getQuantity(), trade);

        entity.setQuantity(newQuantity);
        entity.setAveragePrice(newAveragePrice);
        entity.setFiatCurrency(trade.getFiatCurrency());
        entity.setUpdatedAt(OffsetDateTime.now());

        return toModel(holdingRepository.update(entity));
    }

    @Override
    public boolean hasHolding(UUID userId, String cryptocurrencySymbol) {
        return holdingRepository.hasHolding(userId, cryptocurrencySymbol);
    }

    @Override
    public List<Holding> getHoldings(UUID userId) {
        log.info("Getting holdings for user {}", userId);
        return holdingRepository.findByUserId(userId).stream()
                .map(HoldingUtil::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Holding> getHolding(UUID userId, String cryptocurrencySymbol) {
        return holdingRepository.findByUserIdAndCryptocurrencySymbol(userId, cryptocurrencySymbol)
                .map(HoldingUtil::toModel);
    }

    @Override
    public boolean hasSufficientHolding(UUID userId, String cryptocurrencySymbol, double quantity) {
        return getHolding(userId, cryptocurrencySymbol)
                .map(holding -> holding.getQuantity() >= quantity)
                .orElse(false);
    }

    private HoldingEntity getOrCreateHolding(Trade trade) {
        return holdingRepository.findByUserIdAndCryptocurrencySymbol(
                trade.getUserId(),
                trade.getCryptocurrencySymbol()
        ).orElseGet(() -> initializeHolding(trade));
    }

    private HoldingEntity initializeHolding(Trade trade) {
        return holdingRepository.save(toEntity(trade));
    }

    private static double getQuantityChange(Trade trade) {
        return switch (trade.getOrderType()) {
            case BUY -> trade.getQuantity();
            case SELL -> -trade.getQuantity();
            case null -> throw new IllegalArgumentException("Unsupported order type: " + trade.getOrderType());
        };
    }

    private double calculateNewQuantity(HoldingEntity entity, double quantityChange) {
        return entity.getQuantity() + quantityChange;
    }

    private double calculateNewAveragePrice(double averagePrice, double currentQuantity, Trade trade) {
        return switch (trade.getOrderType()) {
            case BUY ->
                    (((currentQuantity * averagePrice) + (trade.getQuantity() * trade.getPricePerUnit())) / currentQuantity + trade.getQuantity());
            case SELL -> averagePrice;
            case null -> throw new IllegalArgumentException("Unsupported order type: " + trade.getOrderType());
        };
    }
}
