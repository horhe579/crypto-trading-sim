package com.trading.cryptotradingsim.cryptotradingsimbe.config;

import com.trading.cryptotradingsim.cryptotradingsimbe.repository.coin.CoinDataRepository;
import com.trading.cryptotradingsim.cryptotradingsimbe.repository.coin.InMemoryCoinDataRepository;
import com.trading.cryptotradingsim.cryptotradingsimbe.repository.holding.HoldingRepository;
import com.trading.cryptotradingsim.cryptotradingsimbe.repository.holding.SimpleHoldingRepository;
import com.trading.cryptotradingsim.cryptotradingsimbe.repository.trade.SimpleTradeRepository;
import com.trading.cryptotradingsim.cryptotradingsimbe.repository.trade.TradeRepository;
import com.trading.cryptotradingsim.cryptotradingsimbe.repository.user.SimpleUserRepository;
import com.trading.cryptotradingsim.cryptotradingsimbe.repository.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class RepositoryConfiguration {

    @Bean
    public CoinDataRepository coinDataRepository(JdbcTemplate jdbcTemplate) {
        return new InMemoryCoinDataRepository();
    }

    @Bean
    public UserRepository userRepository(JdbcTemplate jdbcTemplate) {
        return new SimpleUserRepository(jdbcTemplate);
    }

    @Bean
    public HoldingRepository holdingRepository(JdbcTemplate jdbcTemplate) {
        return new SimpleHoldingRepository(jdbcTemplate);
    }

    @Bean
    public TradeRepository tradeRepository(JdbcTemplate jdbcTemplate) {
        return new SimpleTradeRepository(jdbcTemplate);
    }
}
