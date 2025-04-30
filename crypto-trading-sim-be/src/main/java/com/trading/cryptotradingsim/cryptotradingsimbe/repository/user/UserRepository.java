package com.trading.cryptotradingsim.cryptotradingsimbe.repository.user;

import com.trading.cryptotradingsim.cryptotradingsimbe.dto.model.User;
import com.trading.cryptotradingsim.cryptotradingsimbe.repository.SimpleRepository;

import java.util.UUID;

public interface UserRepository extends SimpleRepository<User, UUID> {
}
