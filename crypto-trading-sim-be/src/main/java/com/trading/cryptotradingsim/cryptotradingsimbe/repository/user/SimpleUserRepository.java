package com.trading.cryptotradingsim.cryptotradingsimbe.repository.user;

import com.trading.cryptotradingsim.cryptotradingsimbe.dto.entity.UserEntity;
import com.trading.cryptotradingsim.cryptotradingsimbe.exception.BadRequestException;
import com.trading.cryptotradingsim.cryptotradingsimbe.repository.SimpleJdbcRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.time.OffsetDateTime;
import java.util.UUID;

import static com.trading.cryptotradingsim.cryptotradingsimbe.util.RepositoryUtil.addSafeParameter;

public class SimpleUserRepository extends SimpleJdbcRepository<UserEntity, UUID> implements UserRepository {

    private static final String UPDATE_SQL = "UPDATE users SET balance = ? WHERE id = ?";
    private static final String CHECK_FUNDS_SQL = "SELECT balance >= ? FROM users WHERE id = ?";

    public SimpleUserRepository(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate,
                "users",
                "id",
                new BeanPropertyRowMapper<>(UserEntity.class),
                UserEntity.class,
                UUID.class);
    }

    @Override
    public UserEntity save(UserEntity user) {
        if (user.getId() == null) {
            throw new BadRequestException("User ID cannot be null");
        }

        MapSqlParameterSource params = new MapSqlParameterSource();
        addSafeParameter(params, "id", user.getId());
        addSafeParameter(params, "balance", user.getBalance());

        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName(getTableName());

        jdbcInsert.execute(params);

        return user;
    }

    @Override
    public UserEntity update(UserEntity entity) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        addSafeParameter(params, "balance", entity.getBalance());
        addSafeParameter(params, "id", entity.getId());

        getJdbcTemplate().update(
                UPDATE_SQL,
                params
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
