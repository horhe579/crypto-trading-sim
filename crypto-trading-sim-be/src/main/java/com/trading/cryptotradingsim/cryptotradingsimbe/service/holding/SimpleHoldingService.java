package com.trading.cryptotradingsim.cryptotradingsimbe.service.holding;

import com.trading.cryptotradingsim.cryptotradingsimbe.dto.entity.HoldingEntity;
import com.trading.cryptotradingsim.cryptotradingsimbe.dto.model.Holding;
import com.trading.cryptotradingsim.cryptotradingsimbe.dto.model.Trade;
import com.trading.cryptotradingsim.cryptotradingsimbe.repository.holding.HoldingRepository;
import com.trading.cryptotradingsim.cryptotradingsimbe.util.HoldingUtil;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.trading.cryptotradingsim.cryptotradingsimbe.util.HoldingUtil.toEntity;
import static com.trading.cryptotradingsim.cryptotradingsimbe.util.HoldingUtil.toModel;

@Service
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
    @Override
    public Holding updateHolding(Trade trade) {
        HoldingEntity entity = holdingRepository.findByUserIdAndCryptocurrencySymbol(
                trade.getUserId(),
                trade.getCryptocurrencySymbol()
        ).orElseGet(() -> initializeHolding(trade));

        double newQuantity = entity.getQuantity() + trade.getQuantity();
        double newAveragePrice;

        if (trade.getQuantity() > 0) {
            newAveragePrice = calculateNewAveragePrice(entity.getAveragePrice(), entity.getQuantity(), trade);
        } else {
            newAveragePrice = entity.getAveragePrice();
        }

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

    private HoldingEntity initializeHolding(Trade trade) {
        return holdingRepository.save(toEntity(trade));
    }

    private double calculateNewAveragePrice(double averagePrice, double currentQuantity, Trade trade) {
        double newQuantity = currentQuantity + trade.getQuantity();
        return ((currentQuantity * averagePrice) +
                (trade.getQuantity() * trade.getPricePerUnit())) / newQuantity;
    }
}
