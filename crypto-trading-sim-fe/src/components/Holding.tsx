import { useContext } from 'react'
import { CryptoTickerContext } from '../contexts/CryptoTickerContext'
import { MarketCapContext } from '../contexts/MarketCapContext'
import { HoldingResponse } from '../types/HoldingResponse'
import AnimatedPrice from './AnimatedPrice'

interface HoldingProps {
  holding: HoldingResponse
}

const Holding = ({ holding }: HoldingProps) => {
  const tickerContext = useContext(CryptoTickerContext)
  const marketCapContext = useContext(MarketCapContext)

  const coinCode = holding.cryptocurrencySymbol.toUpperCase()
  const tickerData = tickerContext?.ticks[coinCode]
  const coinMetadata = marketCapContext?.topCoins.find(coin => coin.code === coinCode)
  const totalValue = tickerData ? Number((tickerData.lastPrice * holding.quantity).toFixed(2)) : 0

  return (
    <div className="bg-white/10 p-6 rounded-lg">
      <div className="flex justify-between items-center">
        <div className="flex items-center gap-3">
          {coinMetadata?.icon && (
            <img
              src={coinMetadata.icon}
              alt={coinCode}
              className="w-12 h-12"
            />
          )}
          <h2 className="text-xl font-semibold text-white">{coinCode}</h2>
        </div>
        <div className="text-center">
          <p className="text-white text-xl font-bold">
            $<AnimatedPrice price={totalValue} />
          </p>
          <p className="text-gray-400 text-sm">Total Value</p>
        </div>
        <div className="text-right">
          <p className="text-gray-300 text-lg font-bold">Quantity: {holding.quantity}</p>
          {tickerData && (
            <p className="text-gray-300">
              Current Price: $<AnimatedPrice price={tickerData.lastPrice} />
            </p>
          )}
          <p className="text-gray-300">Average Price: {holding.averagePrice} {holding.fiatCurrency}</p>
        </div>
      </div>
    </div>
  )
}

export default Holding 