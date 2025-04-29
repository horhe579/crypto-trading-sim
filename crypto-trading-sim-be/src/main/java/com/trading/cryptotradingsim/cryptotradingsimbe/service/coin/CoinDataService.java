package com.trading.cryptotradingsim.cryptotradingsimbe.service.coin;

import com.trading.cryptotradingsim.cryptotradingsimbe.dto.TickerData;

import java.util.List;

public interface CoinDataService {

    Double getLastPrice(String currencyPair);

    List<Double> getPriceHistory(String currencyPair);

    TickerData updatePrice(TickerData price);
}
