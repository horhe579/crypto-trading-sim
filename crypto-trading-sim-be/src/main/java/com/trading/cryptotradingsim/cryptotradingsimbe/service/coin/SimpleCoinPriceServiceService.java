package com.trading.cryptotradingsim.cryptotradingsimbe.service.coin;

import com.trading.cryptotradingsim.cryptotradingsimbe.dto.TickerData;
import com.trading.cryptotradingsim.cryptotradingsimbe.exception.NotFoundException;
import com.trading.cryptotradingsim.cryptotradingsimbe.repository.coin.CoinDataRepository;

import java.util.List;

public class SimpleCoinPriceServiceService implements CoinDataService {

    private final CoinDataRepository coinDataRepository;

    public SimpleCoinPriceServiceService(CoinDataRepository coinDataRepository) {
        this.coinDataRepository = coinDataRepository;
    }

    @Override
    public Double getLastPrice(String currencyPair) {
        TickerData coinData = coinDataRepository.getById(currencyPair)
                .orElseThrow(() -> new NotFoundException("Price for pair " + currencyPair + " not available"));
        return coinData.last();
    }

    @Override
    public List<Double> getPriceHistory(String currencyPair) {
        List<TickerData> history = coinDataRepository.getHistory(currencyPair);
        return history.stream().map(TickerData::last).toList();
    }

    @Override
    public TickerData updatePrice(TickerData price) {
        return coinDataRepository.save(price);
    }
}
