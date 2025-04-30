package com.trading.cryptotradingsim.cryptotradingsimbe.service.user;

import com.trading.cryptotradingsim.cryptotradingsimbe.dto.model.User;

import java.util.UUID;

public interface UserService {
    User getUser(UUID userId);

    User createUser(User user);

    User getOrCreateUser(UUID userId);

    User updateUser(User user);

    User resetUser(UUID userId);

    // TODO: Do I need to rly expose this?
    User deleteUser(UUID userId);
}
