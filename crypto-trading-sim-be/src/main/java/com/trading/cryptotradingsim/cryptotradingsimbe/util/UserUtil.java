package com.trading.cryptotradingsim.cryptotradingsimbe.util;

import com.trading.cryptotradingsim.cryptotradingsimbe.dto.entity.UserEntity;
import com.trading.cryptotradingsim.cryptotradingsimbe.dto.model.User;

public class UserUtil {
    public static User toModel(UserEntity entity) {
        User user = new User();
        user.setId(entity.getId());
        user.setBalance(entity.getBalance());
        user.setCreatedAt(entity.getCreatedAt());
        return user;
    }

    public static UserEntity toEntity(User model) {
        UserEntity entity = new UserEntity();
        entity.setId(model.getId());
        entity.setBalance(model.getBalance());
        entity.setCreatedAt(model.getCreatedAt());
        return entity;
    }
}
