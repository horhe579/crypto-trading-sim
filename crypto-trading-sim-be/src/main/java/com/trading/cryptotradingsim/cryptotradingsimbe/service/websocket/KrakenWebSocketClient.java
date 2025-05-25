package com.trading.cryptotradingsim.cryptotradingsimbe.service.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.trading.cryptotradingsim.cryptotradingsimbe.dto.KrakenSubscribeMessage;
import com.trading.cryptotradingsim.cryptotradingsimbe.dto.TickerMessage;
import com.trading.cryptotradingsim.cryptotradingsimbe.service.coin.CoinDataService;
import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.trading.cryptotradingsim.cryptotradingsimbe.util.ConstantHolder.OBJECT_MAPPER;
import static com.trading.cryptotradingsim.cryptotradingsimbe.util.JsonUtil.fromMessage;
import static com.trading.cryptotradingsim.cryptotradingsimbe.util.TickerMessageUtil.parseTickerMessage;

@Slf4j
@ClientEndpoint
@Data
public class KrakenWebSocketClient {

    private static final List<String> TOP_20_COINS = List.of(
            "BTC/USD",
            "ETH/USD",
            "USDT/USD",
            "XRP/USD",
            "BNB/USD",
            "SOL/USD",
            "USDC/USD",
            "DOGE/USD",
            "ADA/USD",
            "TRX/USD",
            "STETH/USD",
            "WBTC/USD",
            "SUI/USD",
            "LINK/USD",
            "AVAX/USD",
            "XLM/USD",
            "LEO/USD",
            "TON/USD",
            "SHIB/USD",
            "HBAR/USD"
    );

    // Persist on BE
    private CoinDataService coinDataService;

    @OnOpen
    public void onOpen(Session session) throws JsonProcessingException {
        log.info("Connected to Kraken websocket");
        // TODO: fetch top 20 prices BE, use them to subscribe to websocket
        KrakenSubscribeMessage subscribeMessage = new KrakenSubscribeMessage(TOP_20_COINS);
        session.getAsyncRemote().sendText(OBJECT_MAPPER.writeValueAsString(subscribeMessage));
    }

    @OnMessage
    public void onMessage(String message) throws JsonProcessingException {
        try {
            // Currently not keeping track of how many coins I subscribed to with the message below
            // {"method":"subscribe","result":{"channel":"ticker","event_trigger":"trades","snapshot":true,"symbol":"BTC/USD"},"success":true,"time_in":"2025-04-29T17:21:44.462894Z","time_out":"2025-04-29T17:21:44.462942Z"}
            JsonNode jsonMessage = fromMessage(message);
            TickerMessage tickerMessage = parseTickerMessage(jsonMessage);
            coinDataService.updatePrice(tickerMessage.data().getFirst());
        } catch (JsonProcessingException | IllegalArgumentException e) {
//            log.warn("Could not parse a ws message to a ticker message, reason {}", e.getMessage());
        }
    }
}
