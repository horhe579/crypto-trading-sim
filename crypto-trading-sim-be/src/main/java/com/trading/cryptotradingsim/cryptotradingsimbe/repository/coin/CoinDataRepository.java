package com.trading.cryptotradingsim.cryptotradingsimbe.repository.coin;

import com.trading.cryptotradingsim.cryptotradingsimbe.dto.TickerData;
import com.trading.cryptotradingsim.cryptotradingsimbe.repository.SimpleRepository;

import java.util.List;

public interface CoinDataRepository extends SimpleRepository<TickerData, String> {
    List<TickerData> getHistory(String currencyPair);
}
