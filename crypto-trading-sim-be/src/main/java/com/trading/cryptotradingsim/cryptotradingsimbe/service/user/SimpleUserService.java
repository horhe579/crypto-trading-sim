package com.trading.cryptotradingsim.cryptotradingsimbe.service.user;

import com.trading.cryptotradingsim.cryptotradingsimbe.dto.model.User;
import com.trading.cryptotradingsim.cryptotradingsimbe.exception.NotFoundException;
import com.trading.cryptotradingsim.cryptotradingsimbe.repository.user.UserRepository;

import java.time.OffsetDateTime;
import java.util.UUID;

import static com.trading.cryptotradingsim.cryptotradingsimbe.util.ConstantHolder.DEFAULT_BALANCE;

public class SimpleUserService implements UserService {

    private final UserRepository userRepository;

    public SimpleUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUser(UUID userId) {
        return userRepository.getById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %s not found", userId)));
    }

    @Override
    public User getOrCreateUser(UUID userId) {
        return userRepository.getById(userId)
                .orElseGet(() -> userRepository.save(createDefaultUser(userId)));
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.update(user);
    }

    @Override
    public User resetUser(UUID userId) {
        User user = userRepository.getById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %s not found", userId)));
        // Clear transactions, holdings, reset balance(OR DELETE AND CREATE USER WITH SAME ID)
        user.setBalance(DEFAULT_BALANCE);
        return userRepository.save(user);
    }

    @Override
    public User deleteUser(UUID userId) {
        User user = getUser(userId);
        userRepository.deleteById(userId);
        return user;
    }

    private User createDefaultUser(UUID userId) {
        return new User(userId, DEFAULT_BALANCE, OffsetDateTime.now());
    }
}