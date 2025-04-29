package com.trading.cryptotradingsim.cryptotradingsimbe.service.websocket;

import com.trading.cryptotradingsim.cryptotradingsimbe.dto.TickerMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class CoinPriceBroadcastService {

    private final SimpMessagingTemplate messagingTemplate;

    public CoinPriceBroadcastService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void broadcastPrice(TickerMessage update) {
        messagingTemplate.convertAndSend("/topic/prices", update);
    }
}
