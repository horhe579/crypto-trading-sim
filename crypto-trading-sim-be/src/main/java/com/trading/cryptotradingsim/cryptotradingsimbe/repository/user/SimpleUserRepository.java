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

    private static final String INSERT_IF_ABSENT_SQL =
            "INSERT INTO users " +
                    "(id, " +
                    "balance) " +
                    "VALUES (?, ?) " +
                    "ON CONFLICT (id) DO NOTHING";


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
        return saveUser(user, INSERT_SQL);
    }

    @Override
    public UserEntity saveIfAbsent(UserEntity user) {
        return saveUser(user, INSERT_IF_ABSENT_SQL);
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

    private UserEntity saveUser(UserEntity user, String insertSql) {
        if (user.getId() == null) {
            throw new BadRequestException("User ID cannot be null");
        }

        int rowsAffected = getJdbcTemplate().update(insertSql,
                user.getId(),
                user.getBalance()
        );
        if (rowsAffected == 0) {
            return getById(user.getId()).orElseThrow(
                    () -> new RuntimeException("User should exist but wasn't found after save"));
        }
        return user;
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
