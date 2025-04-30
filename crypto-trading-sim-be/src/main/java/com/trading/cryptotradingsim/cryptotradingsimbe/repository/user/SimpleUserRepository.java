package com.trading.cryptotradingsim.cryptotradingsimbe.repository.user;

import com.trading.cryptotradingsim.cryptotradingsimbe.dto.model.User;
import com.trading.cryptotradingsim.cryptotradingsimbe.repository.SimpleJdbcRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.UUID;


public class SimpleUserRepository extends SimpleJdbcRepository<User, UUID> implements UserRepository {

    public SimpleUserRepository(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate,
                "users",
                "id",
                new BeanPropertyRowMapper<>(User.class),
                User.class,
                UUID.class);
    }
}
