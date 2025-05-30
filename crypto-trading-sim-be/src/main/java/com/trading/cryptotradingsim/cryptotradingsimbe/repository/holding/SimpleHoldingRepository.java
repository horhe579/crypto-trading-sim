package com.trading.cryptotradingsim.cryptotradingsimbe.repository.holding;

import com.trading.cryptotradingsim.cryptotradingsimbe.dto.entity.HoldingEntity;
import com.trading.cryptotradingsim.cryptotradingsimbe.repository.SimpleJdbcRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SimpleHoldingRepository extends SimpleJdbcRepository<HoldingEntity, UUID> implements HoldingRepository {

    private static final String UPDATE_SQL =
            "UPDATE holdings " +
                    "SET quantity = ?, " +
                    "average_price = ?, " +
                    "fiat_currency = ?, " +
                    "updated_at = ? " +
                    "WHERE id = ?::uuid";

    private static final String HAS_HOLDING_SQL =
            "SELECT COUNT(*) FROM holdings " +
                    "WHERE user_id = ?::uuid " +
                    "AND cryptocurrency_symbol = ?";

    private static final String FIND_BY_USER_AND_SYMBOL_SQL =
            "SELECT * FROM holdings " +
                    "WHERE user_id = ?::uuid " +
                    "AND cryptocurrency_symbol = ?";

    private static final String FIND_BY_USER_SQL =
            "SELECT * FROM holdings " +
                    "WHERE user_id = ?::uuid";

    private static final String INSERT_SQL =
            "INSERT INTO holdings " +
                    "(id, " +
                    "user_id, " +
                    "cryptocurrency_symbol, " +
                    "quantity, " +
                    "average_price, " +
                    "fiat_currency, " +
                    "updated_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String INSERT_IF_ABSENT_SQL =
            "INSERT INTO holdings " +
                    "(id, " +
                    "user_id, " +
                    "cryptocurrency_symbol, " +
                    "quantity, " +
                    "average_price, " +
                    "fiat_currency, " +
                    "updated_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                    "ON CONFLICT (user_id, cryptocurrency_symbol) DO NOTHING";

    public SimpleHoldingRepository(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate,
                "holdings",
                "id",
                createHoldingRowMapper(),
                HoldingEntity.class,
                UUID.class);
    }

    @Override
    public HoldingEntity save(HoldingEntity entity) {
        return saveHolding(entity, INSERT_SQL);
    }

    @Override
    public HoldingEntity saveIfAbsent(HoldingEntity holding) {
        return saveHolding(holding, INSERT_IF_ABSENT_SQL);
    }

    @Override
    public HoldingEntity update(HoldingEntity entity) {
        getJdbcTemplate().update(
                UPDATE_SQL,
                entity.getQuantity(),
                entity.getAveragePrice(),
                entity.getFiatCurrency(),
                entity.getUpdatedAt(),
                entity.getId()
        );
        return entity;
    }

    @Override
    public boolean hasHolding(UUID userId, String cryptocurrencySymbol) {
        Integer count = getJdbcTemplate().queryForObject(
                HAS_HOLDING_SQL,
                Integer.class,
                userId.toString(),
                cryptocurrencySymbol
        );
        return count != null && count > 0;
    }

    @Override
    public Optional<HoldingEntity> findByUserIdAndCryptocurrencySymbol(UUID userId, String cryptocurrencySymbol) {
        List<HoldingEntity> holdings = getJdbcTemplate().query(
                FIND_BY_USER_AND_SYMBOL_SQL,
                createHoldingRowMapper(),
                userId.toString(),
                cryptocurrencySymbol
        );
        return holdings.isEmpty() ? Optional.empty() : Optional.of(holdings.get(0));
    }

    @Override
    public List<HoldingEntity> findByUserId(UUID userId) {
        return getJdbcTemplate().query(
                FIND_BY_USER_SQL,
                createHoldingRowMapper(),
                userId.toString()
        );
    }

    private HoldingEntity saveHolding(HoldingEntity holding, String insertSql) {
        if (holding.getId() == null) {
            holding.setId(UUID.randomUUID());
        }

        int rowsAffected = getJdbcTemplate().update(insertSql,
                holding.getId(),
                holding.getUserId(),
                holding.getCryptocurrencySymbol(),
                holding.getQuantity(),
                holding.getAveragePrice(),
                holding.getFiatCurrency(),
                holding.getUpdatedAt()
        );
        if (rowsAffected == 0) {
            return getById(holding.getId()).orElseThrow(
                    () -> new RuntimeException("Holding should exist but wasn't found after save"));
        }
        return holding;
    }

    private static RowMapper<HoldingEntity> createHoldingRowMapper() {
        return (rs, rowNum) -> {
            HoldingEntity holding = new HoldingEntity();
            holding.setId(UUID.fromString(rs.getString("id")));
            holding.setUserId(UUID.fromString(rs.getString("user_id")));
            holding.setCryptocurrencySymbol(rs.getString("cryptocurrency_symbol"));
            holding.setQuantity(rs.getDouble("quantity"));
            holding.setAveragePrice(rs.getDouble("average_price"));
            holding.setFiatCurrency(rs.getString("fiat_currency"));

            java.sql.Timestamp timestamp = rs.getTimestamp("updated_at");
            if (timestamp != null) {
                holding.setUpdatedAt(timestamp.toInstant().atOffset(OffsetDateTime.now().getOffset()));
            }

            return holding;
        };
    }
} 