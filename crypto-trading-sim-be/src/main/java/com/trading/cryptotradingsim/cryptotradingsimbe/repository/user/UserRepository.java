package com.trading.cryptotradingsim.cryptotradingsimbe.repository.user;

import com.trading.cryptotradingsim.cryptotradingsimbe.dto.entity.UserEntity;
import com.trading.cryptotradingsim.cryptotradingsimbe.repository.SimpleRepository;

import java.util.UUID;

public interface UserRepository extends SimpleRepository<UserEntity, UUID> {
    UserEntity saveIfAbsent(UserEntity user);

    boolean hasSufficientFunds(UUID userId, double amount);
}
