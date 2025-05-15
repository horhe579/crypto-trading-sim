package com.trading.cryptotradingsim.cryptotradingsimbe.repository.trade;

import com.trading.cryptotradingsim.cryptotradingsimbe.dto.OrderType;
import com.trading.cryptotradingsim.cryptotradingsimbe.dto.entity.TradeEntity;
import com.trading.cryptotradingsim.cryptotradingsimbe.repository.SimpleJdbcRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.util.UUID;

public class SimpleTradeRepository extends SimpleJdbcRepository<TradeEntity, UUID> implements TradeRepository {

    private static final String UPDATE_SQL =
            "UPDATE trades " +
                    "SET cryptocurrency_symbol = ?, " +
                    "quantity = ?, " +
                    "trade_type = ?::trade_type, " +
                    "price_per_unit = ?, " +
                    "user_id = ?, " +
                    "fiat_currency = ?, " +
                    "timestamp = ?::timestamptz, " +
                    "profit_loss = ? " +
                    "WHERE id = ?::uuid";

    private static final String INSERT_SQL =
            "INSERT INTO trades " +
                    "(id," +
                    " user_id," +
                    " trade_type," +
                    " cryptocurrency_symbol," +
                    " quantity," +
                    " price_per_unit," +
                    " fiat_currency," +
                    " profit_loss," +
                    " timestamp) " +
                    "VALUES (?::uuid, ?, ?::trade_type, ?, ?, ?, ?, ?, ?::timestamptz)";

    public SimpleTradeRepository(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate,
                "trades",
                "id",
                createTradeRowMapper(),
                TradeEntity.class,
                UUID.class);
    }

    @Override
    public TradeEntity save(TradeEntity entity) {
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID());
        }

        getJdbcTemplate().update(INSERT_SQL,
                entity.getId(),
                entity.getUserId(),
                entity.getTradeType().name(),
                entity.getCryptocurrencySymbol(),
                entity.getQuantity(),
                entity.getPricePerUnit(),
                entity.getFiatCurrency(),
                entity.getProfitLoss(),
                entity.getTimestamp() != null ?
                        java.sql.Timestamp.from(entity.getTimestamp()) :
                        new java.sql.Timestamp(System.currentTimeMillis())
        );

        return entity;
    }

    // No one would update a trade entry so I might just throw not implemente here
    @Override
    public TradeEntity update(TradeEntity entity) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        getJdbcTemplate().update(
                UPDATE_SQL,
                entity.getCryptocurrencySymbol(),
                entity.getQuantity(),
                entity.getTradeType().name(),
                entity.getPricePerUnit(),
                entity.getFiatCurrency(),
                entity.getProfitLoss(),
                entity.getId()
        );
        return entity;
    }

    private static RowMapper<TradeEntity> createTradeRowMapper() {
        return (rs, rowNum) -> {
            TradeEntity trade = new TradeEntity();
            trade.setId(UUID.fromString(rs.getString("id")));
            trade.setUserId(UUID.fromString(rs.getString("user_id")));
            trade.setTradeType(OrderType.valueOf(rs.getString("trade_type")));
            trade.setCryptocurrencySymbol(rs.getString("cryptocurrency_symbol"));
            trade.setQuantity(rs.getDouble("quantity"));
            trade.setPricePerUnit(rs.getDouble("price_per_unit"));
            trade.setFiatCurrency(rs.getString("fiat_currency"));
            trade.setProfitLoss(rs.getDouble("profit_loss"));

            java.sql.Timestamp timestamp = rs.getTimestamp("timestamp");
            if (timestamp != null) {
                trade.setTimestamp(timestamp.toInstant());
            }

            return trade;
        };
    }
}
