package com.trading.cryptotradingsim.cryptotradingsimbe.repository.user;

import com.trading.cryptotradingsim.cryptotradingsimbe.dto.model.User;
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


public class SimpleUserRepository extends SimpleJdbcRepository<User, UUID> implements UserRepository {

    public SimpleUserRepository(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate,
                "users",
                "id",
                new BeanPropertyRowMapper<>(User.class),
                User.class,
                UUID.class);
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            throw new BadRequestException("User ID cannot be null");
        }

        MapSqlParameterSource params = new MapSqlParameterSource();
        addSafeParameter(params, "id", user.getId());
        addSafeParameter(params, "balance", user.getBalance());
        addSafeParameter(params, "created_at", user.getCreatedAt());

        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName(getTableName());

        jdbcInsert.execute(params);

        return user;
    }

    @Override
    public User update(User entity) {
        // Use explicit SQL types to avoid inference issues
        MapSqlParameterSource params = new MapSqlParameterSource();
        addSafeParameter(params, "balance", entity.getBalance());
        addSafeParameter(params, "id", entity.getId());

        getJdbcTemplate().update(
                "UPDATE users SET balance = ? WHERE id = ?",
                entity.getBalance(),
                entity.getId()
        );

        return entity;
    }

    private static RowMapper<User> createUserRowMapper() {
        return (rs, rowNum) -> {
            User user = new User();
            user.setId(UUID.fromString(rs.getString("id")));
            user.setBalance(rs.getDouble("balance"));

            // Safely handle timestamp conversion
            java.sql.Timestamp timestamp = rs.getTimestamp("created_at");
            if (timestamp != null) {
                user.setCreatedAt(timestamp.toInstant().atOffset(OffsetDateTime.now().getOffset()));
            }

            return user;
        };
    }
}
