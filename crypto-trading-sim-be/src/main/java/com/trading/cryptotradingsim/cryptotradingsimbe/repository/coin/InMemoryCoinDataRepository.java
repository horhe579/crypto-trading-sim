package com.trading.cryptotradingsim.cryptotradingsimbe.repository.coin;

import com.trading.cryptotradingsim.cryptotradingsimbe.dto.TickerData;
import com.trading.cryptotradingsim.cryptotradingsimbe.exception.NotImplementedException;
import lombok.extern.slf4j.Slf4j;

import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

@Slf4j
public class InMemoryCoinDataRepository implements CoinDataRepository {

    private final Map<String, Deque<TickerData>> prices = new ConcurrentHashMap<>();

    @Override
    public TickerData save(TickerData tickerData) {

        String currencyPair = tickerData.currencyPair();

        Deque<TickerData> history = prices
                .computeIfAbsent(currencyPair, k -> new LinkedBlockingDeque<>());

        history.addFirst(tickerData);

//        log.info("Updated price for {}, current price: {} $", currencyPair, tickerData.last());
        return tickerData;
    }

    @Override
    public TickerData update(TickerData tickerData) {
        throw new NotImplementedException("Updating price history is not implemented.");
    }

    // TODO: Implement handling queries for not persisted coins
    // EX: Fetch top 20, user buys last coin; Reboot; Fetch again - Top 20 updated, last coin went away;
    // User wants to make a transaction with last coin, but no data is found.
    @Override
    public Optional<TickerData> getById(String currencyPair) {
        Deque<TickerData> history = prices.get(currencyPair);
        return history != null && !history.isEmpty() ?
                Optional.of(history.getFirst()) : Optional.empty();
    }

    @Override
    public List<TickerData> getAll() {
        return prices.values().stream().flatMap(Deque::stream).toList();
    }

    @Override
    public void deleteById(String currencyPair) {
        throw new NotImplementedException("Deleting prices is not implemented.");
    }

    @Override
    public List<TickerData> getHistory(String currencyPair) {
        Deque<TickerData> history = prices.get(currencyPair);
        return history == null ? List.of() : List.copyOf(history);
    }
}
