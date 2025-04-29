package com.trading.cryptotradingsim.cryptotradingsimbe.service.websocket;

import com.trading.cryptotradingsim.cryptotradingsimbe.service.coin.CoinDataService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;

@Slf4j
public class KrakenWebSocketSubscriber {

    private final String WS_URL;
    private final CoinDataService coinDataService;
    private Session session;

    public KrakenWebSocketSubscriber(String wsUrl, CoinDataService coinDataService) {
        WS_URL = wsUrl;
        this.coinDataService = coinDataService;
    }

    @PostConstruct
    public void subscribe() {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try {
            KrakenWebSocketClient client = initializeClient();
            session = container.connectToServer(client, URI.create(WS_URL));
        } catch (Exception e) {
            log.error("Error connecting to kraken websocket", e);
        }
    }

    @PreDestroy
    public void cleanup() {
        if (session != null && session.isOpen()) {
            try {
                session.close();
                log.info("Kraken WebSocket session closed.");
            } catch (Exception e) {
                log.error("Error closing Kraken WebSocket session", e);
            }
        }
    }

    private KrakenWebSocketClient initializeClient() {
        KrakenWebSocketClient client = new KrakenWebSocketClient();
        client.setCoinDataService(coinDataService);
        return client;
    }
}
