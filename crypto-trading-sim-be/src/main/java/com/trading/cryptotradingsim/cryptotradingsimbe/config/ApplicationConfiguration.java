package com.trading.cryptotradingsim.cryptotradingsimbe.config;

import com.trading.cryptotradingsim.cryptotradingsimbe.repository.coin.CoinDataRepository;
import com.trading.cryptotradingsim.cryptotradingsimbe.repository.user.UserRepository;
import com.trading.cryptotradingsim.cryptotradingsimbe.service.coin.CoinDataService;
import com.trading.cryptotradingsim.cryptotradingsimbe.service.coin.SimpleCoinPriceServiceService;
import com.trading.cryptotradingsim.cryptotradingsimbe.service.trade.OrderService;
import com.trading.cryptotradingsim.cryptotradingsimbe.service.trade.SynchronousOrderService;
import com.trading.cryptotradingsim.cryptotradingsimbe.service.user.SimpleUserService;
import com.trading.cryptotradingsim.cryptotradingsimbe.service.user.UserService;
import com.trading.cryptotradingsim.cryptotradingsimbe.service.websocket.KrakenWebSocketSubscriber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public CoinDataService coinPriceService(CoinDataRepository coinDataRepository) {
        return new SimpleCoinPriceServiceService(coinDataRepository);
    }

    @Bean
    public OrderService orderService(CoinDataService coinDataService, UserService userService) {
        return new SynchronousOrderService(coinDataService, userService);
    }

    @Bean
    public UserService userService(UserRepository userRepository) {
        return new SimpleUserService(userRepository);
    }

    @Bean
    public KrakenWebSocketSubscriber krakenWebSocketSubscriber(@Value("${kraken.ticker.ws.url}") String wsUrl,
                                                               CoinDataService coinDataService) {
        return new KrakenWebSocketSubscriber(wsUrl, coinDataService);
    }
}