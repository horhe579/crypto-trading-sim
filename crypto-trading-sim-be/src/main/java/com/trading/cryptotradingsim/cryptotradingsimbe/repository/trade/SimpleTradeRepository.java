package com.trading.cryptotradingsim.cryptotradingsimbe.repository.trade;

import com.trading.cryptotradingsim.cryptotradingsimbe.dto.OrderType;
import com.trading.cryptotradingsim.cryptotradingsimbe.dto.entity.TradeEntity;
import com.trading.cryptotradingsim.cryptotradingsimbe.repository.SimpleJdbcRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.time.Instant;
import java.util.UUID;

import static com.trading.cryptotradingsim.cryptotradingsimbe.util.RepositoryUtil.addSafeParameter;

public class SimpleTradeRepository extends SimpleJdbcRepository<TradeEntity, UUID> implements TradeRepository {

    private static final String UPDATE_SQL = "UPDATE trades SET cryptocurrency_symbol = ?, amount = ?, trade_type = ?, " +
            "price_per_unit = ?, user_id = ?, fiat_currency = ?, timestamp = ?, profit_loss = ? " +
            "WHERE id = ?";

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
        MapSqlParameterSource params = new MapSqlParameterSource();
        addSafeParameter(params, "cryptocurrency_symbol", entity.getCryptocurrencySymbol());
        addSafeParameter(params, "amount", entity.getAmount());
        addSafeParameter(params, "trade_type", entity.getTradeType().name());
        addSafeParameter(params, "price_per_unit", entity.getPricePerUnit());
        addSafeParameter(params, "user_id", entity.getUserId());
        addSafeParameter(params, "fiat_currency", entity.getFiatCurrency());
        addSafeParameter(params, "profit_loss", entity.getProfitLoss());

        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName(getTableName());

        jdbcInsert.execute(params);
        return entity;
    }

    @Override
    public TradeEntity update(TradeEntity entity) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        addSafeParameter(params, "cryptocurrency_symbol", entity.getCryptocurrencySymbol());
        addSafeParameter(params, "amount", entity.getAmount());
        addSafeParameter(params, "trade_type", entity.getTradeType().name());
        addSafeParameter(params, "price_per_unit", entity.getPricePerUnit());
        addSafeParameter(params, "fiat_currency", entity.getFiatCurrency());
        addSafeParameter(params, "profit_loss", entity.getProfitLoss());
        addSafeParameter(params, "id", entity.getId());

        getJdbcTemplate().update(
                UPDATE_SQL,
                params
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
            trade.setAmount(rs.getDouble("amount"));
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
