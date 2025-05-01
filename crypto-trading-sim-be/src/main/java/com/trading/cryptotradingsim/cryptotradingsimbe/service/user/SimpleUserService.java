package com.trading.cryptotradingsim.cryptotradingsimbe.service.user;

import com.trading.cryptotradingsim.cryptotradingsimbe.dto.entity.UserEntity;
import com.trading.cryptotradingsim.cryptotradingsimbe.dto.model.User;
import com.trading.cryptotradingsim.cryptotradingsimbe.exception.NotFoundException;
import com.trading.cryptotradingsim.cryptotradingsimbe.repository.user.UserRepository;
import com.trading.cryptotradingsim.cryptotradingsimbe.util.UserUtil;

import java.time.OffsetDateTime;
import java.util.UUID;

import static com.trading.cryptotradingsim.cryptotradingsimbe.util.ConstantHolder.DEFAULT_BALANCE;
import static com.trading.cryptotradingsim.cryptotradingsimbe.util.UserUtil.toEntity;
import static com.trading.cryptotradingsim.cryptotradingsimbe.util.UserUtil.toModel;

public class SimpleUserService implements UserService {

    private final UserRepository userRepository;

    public SimpleUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUser(UUID userId) {
        return userRepository.getById(userId)
                .map(UserUtil::toModel)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %s not found", userId)));
    }

    @Override
    public User getOrCreateUser(UUID userId) {
        return userRepository.getById(userId)
                .map(UserUtil::toModel)
                .orElseGet(() -> {
                    UserEntity defaultUser = createDefaultUser(userId);
                    return toModel(userRepository.save(defaultUser));
                });
    }

    @Override
    public User createUser(User user) {
        return toModel(userRepository.save(toEntity(user)));
    }

    @Override
    public User updateUser(User user) {
        return toModel(userRepository.update(toEntity(user)));
    }

    @Override
    public User resetUser(UUID userId) {
        UserEntity userEntity = userRepository.getById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %s not found", userId)));
        userEntity.setBalance(DEFAULT_BALANCE);
        //TODO: Clear trades, holdings
        return toModel(userRepository.save(userEntity));
    }

    @Override
    public User deleteUser(UUID userId) {
        User user = getUser(userId);
        userRepository.deleteById(userId);
        return user;
    }

    @Override
    public boolean hasSufficientFunds(UUID userId, double amount) {
        return userRepository.hasSufficientFunds(userId, amount);
    }

    private UserEntity createDefaultUser(UUID userId) {
        return new UserEntity(userId, DEFAULT_BALANCE, OffsetDateTime.now());
    }
}