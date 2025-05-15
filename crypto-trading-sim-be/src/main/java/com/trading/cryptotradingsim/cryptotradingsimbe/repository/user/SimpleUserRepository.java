package com.trading.cryptotradingsim.cryptotradingsimbe.repository.user;

import com.trading.cryptotradingsim.cryptotradingsimbe.dto.entity.UserEntity;
import com.trading.cryptotradingsim.cryptotradingsimbe.exception.BadRequestException;
import com.trading.cryptotradingsim.cryptotradingsimbe.repository.SimpleJdbcRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.time.OffsetDateTime;
import java.util.UUID;

public class SimpleUserRepository extends SimpleJdbcRepository<UserEntity, UUID> implements UserRepository {

    private static final String UPDATE_SQL =
            "UPDATE users " +
                    "SET balance = ? " +
                    "WHERE id = ?";

    private static final String CHECK_FUNDS_SQL =
            "SELECT balance >= ? " +
                    "FROM users " +
                    "WHERE id = ?";

    private static final String INSERT_SQL =
            "INSERT INTO users " +
                    "(id, " +
                    "balance) " +
                    "VALUES (?, ?)";


    public SimpleUserRepository(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate,
                "users",
                "id",
                createUserRowMapper(),
                UserEntity.class,
                UUID.class);
    }

    @Override
    public UserEntity save(UserEntity user) {
        if (user.getId() == null) {
            throw new BadRequestException("User ID cannot be null");
        }

        getJdbcTemplate().update(INSERT_SQL,
                user.getId(),
                user.getBalance()
        );

        return user;
    }

    @Override
    public UserEntity update(UserEntity entity) {
        getJdbcTemplate().update(
                UPDATE_SQL,
                entity.getBalance(),
                entity.getId()
        );
        return entity;
    }

    @Override
    public boolean hasSufficientFunds(UUID userId, double amount) {
        Boolean result = getJdbcTemplate().queryForObject(
                CHECK_FUNDS_SQL,
                Boolean.class,
                amount,
                userId
        );
        return result != null && result;
    }

    private static RowMapper<UserEntity> createUserRowMapper() {
        return (rs, rowNum) -> {
            UserEntity user = new UserEntity();
            user.setId(UUID.fromString(rs.getString("id")));
            user.setBalance(rs.getDouble("balance"));

            java.sql.Timestamp timestamp = rs.getTimestamp("created_at");
            if (timestamp != null) {
                user.setCreatedAt(timestamp.toInstant().atOffset(OffsetDateTime.now().getOffset()));
            }

            return user;
        };
    }
}
