package com.trading.cryptotradingsim.cryptotradingsimbe.repository.holding;

import com.trading.cryptotradingsim.cryptotradingsimbe.dto.entity.HoldingEntity;
import com.trading.cryptotradingsim.cryptotradingsimbe.repository.SimpleRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HoldingRepository extends SimpleRepository<HoldingEntity, UUID> {

    boolean hasHolding(UUID userId, String cryptocurrencySymbol);
    
    Optional<HoldingEntity> findByUserIdAndCryptocurrencySymbol(UUID userId, String cryptocurrencySymbol);
    
    List<HoldingEntity> findByUserId(UUID userId);
} 