package com.trading.cryptotradingsim.cryptotradingsimbe.config;

import com.trading.cryptotradingsim.cryptotradingsimbe.repository.coin.CoinDataRepository;
import com.trading.cryptotradingsim.cryptotradingsimbe.repository.coin.InMemoryCoinDataRepository;
import com.trading.cryptotradingsim.cryptotradingsimbe.service.coin.CoinDataService;
import com.trading.cryptotradingsim.cryptotradingsimbe.service.coin.SimpleCoinPriceServiceService;
import com.trading.cryptotradingsim.cryptotradingsimbe.service.websocket.KrakenWebSocketSubscriber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public CoinDataRepository coinDataRepository() {
        return new InMemoryCoinDataRepository();
    }

    @Bean
    public CoinDataService coinPriceService(CoinDataRepository coinDataRepository) {
        return new SimpleCoinPriceServiceService(coinDataRepository);
    }

    @Bean
    public KrakenWebSocketSubscriber krakenWebSocketSubscriber(@Value("${kraken.ticker.ws.url}") String wsUrl,
                                                               CoinDataService coinDataService) {
        return new KrakenWebSocketSubscriber(wsUrl, coinDataService);
    }
}