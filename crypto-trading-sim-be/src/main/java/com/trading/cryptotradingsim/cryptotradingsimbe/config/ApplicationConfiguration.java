package com.trading.cryptotradingsim.cryptotradingsimbe.config;

import com.trading.cryptotradingsim.cryptotradingsimbe.repository.coin.CoinDataRepository;
import com.trading.cryptotradingsim.cryptotradingsimbe.repository.holding.HoldingRepository;
import com.trading.cryptotradingsim.cryptotradingsimbe.repository.trade.TradeRepository;
import com.trading.cryptotradingsim.cryptotradingsimbe.repository.user.UserRepository;
import com.trading.cryptotradingsim.cryptotradingsimbe.service.coin.CoinDataService;
import com.trading.cryptotradingsim.cryptotradingsimbe.service.coin.SimpleCoinPriceServiceService;
import com.trading.cryptotradingsim.cryptotradingsimbe.service.holding.HoldingService;
import com.trading.cryptotradingsim.cryptotradingsimbe.service.holding.SimpleHoldingService;
import com.trading.cryptotradingsim.cryptotradingsimbe.service.trade.AsynchronousOrderService;
import com.trading.cryptotradingsim.cryptotradingsimbe.service.trade.OrderService;
import com.trading.cryptotradingsim.cryptotradingsimbe.service.trade.SimpleTradeService;
import com.trading.cryptotradingsim.cryptotradingsimbe.service.trade.TradeService;
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
    public TradeService tradeService(TradeRepository tradeRepository,
                                     UserService userService,
                                     HoldingService holdingService) {
        return new SimpleTradeService(tradeRepository, userService, holdingService);
    }

    @Bean
    public OrderService orderService(CoinDataService coinDataService,
                                     TradeService tradeService) {
        return new AsynchronousOrderService(coinDataService, tradeService);
    }

    @Bean
    public UserService userService(UserRepository userRepository) {
        return new SimpleUserService(userRepository);
    }

    @Bean
    public HoldingService holdingService(HoldingRepository holdingRepository) {
        return new SimpleHoldingService(holdingRepository);
    }

    @Bean
    public KrakenWebSocketSubscriber krakenWebSocketSubscriber(@Value("${kraken.ticker.ws.url}") String wsUrl,
                                                               CoinDataService coinDataService) {
        return new KrakenWebSocketSubscriber(wsUrl, coinDataService);
    }
}