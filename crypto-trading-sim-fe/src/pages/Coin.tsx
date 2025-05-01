import { useContext, useEffect, useState, useRef } from "react"
import { useParams, useNavigate } from "react-router-dom"
import { CryptoTickerContext } from "../contexts/CryptoTickerContext"
import { MarketCapContext } from "../contexts/MarketCapContext"
import { UserContext } from "../contexts/UserContext"
import { CoinData } from "../types/CoinData"
import { TailSpin } from 'react-loading-icons'
import AnimatedPrice from "../components/AnimatedPrice"
import TradeModal from "../components/TradeModal"
import NotificationManager from "../components/NotificationManager"
import { OrderContext } from "../contexts/OrderContext"
const Coin = () => {
  const { coinCode } = useParams()
  const navigate = useNavigate()
  const tickerContext = useContext(CryptoTickerContext)
  const marketCapContext = useContext(MarketCapContext)
  const userContext = useContext(UserContext)
  const orderContext = useContext(OrderContext)
  const [coinData, setCoinData] = useState<CoinData | null>(null)
  const [isTradeModalOpen, setIsTradeModalOpen] = useState(false)
  const [tradeType, setTradeType] = useState<'buy' | 'sell'>('buy')
  const notificationManagerRef = useRef<{ addNotification: (message: string, type: 'error' | 'success' | 'info' | 'warning', duration?: number) => void }>(null)

  useEffect(() => {
    if (!coinCode) {
      navigate("/")
      return
    }

    // get last coin price and merge with metadata
    const metadata = marketCapContext?.topCoins.find(coin => coin.code === coinCode?.toUpperCase())
    const tickerData = tickerContext?.ticks[coinCode?.toUpperCase() || '']

    if (metadata && tickerData) {
      setCoinData({
        ...metadata,
        coinCode: tickerData.coinCode,
        highestBid: tickerData.highestBid,
        lowestAsk: tickerData.lowestAsk,
        lastPrice: tickerData.lastPrice
      })
    }
  }, [coinCode, tickerContext, marketCapContext, navigate])

  const handleTrade = async (amount: number) => {
    if (!coinData || !userContext?.userId || !marketCapContext?.currency) return

    const endpoint = tradeType === 'buy' ? 'buy' : 'sell'
    const currencyPair = `${coinData.code.toUpperCase()}/${marketCapContext.currency.name.toUpperCase()}`
    
    try {
      const response = await fetch(`${import.meta.env.VITE_BACKEND_SERVICE_URL}/orders/${endpoint}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'X-User-Id': userContext.userId
        },
        body: JSON.stringify({
          currencyPair: currencyPair,
          quantity: amount,
          orderType: tradeType.toUpperCase()
        })
      })

      if (!response.ok) {
        if (response.status === 422) {
          const errorData = await response.json()
          notificationManagerRef.current?.addNotification(errorData.message, 'error')
        } else {
          throw new Error(`Failed to execute ${tradeType} order`)
        }
        return
      }

      const result = await response.json()
      notificationManagerRef.current?.addNotification(`${tradeType.charAt(0).toUpperCase() + tradeType.slice(1)} order executed successfully`, 'success')
      orderContext?.incrementOrderCount()
      console.log(`${tradeType} order executed:`, result)
    } catch (error) {
      console.error(`Error executing ${tradeType} order:`, error)
      notificationManagerRef.current?.addNotification(`Error executing ${tradeType} order`, 'error')
    }
  }

  const openTradeModal = (type: 'buy' | 'sell') => {
    setTradeType(type)
    setIsTradeModalOpen(true)
  }

  return (
    <div className="min-h-screen p-8">
      <NotificationManager ref={notificationManagerRef} />
      <div className="max-w-4xl mx-auto">
        <button
          onClick={() => navigate("/")}
          className="mb-8 text-white hover:text-gray-300 transition-colors flex items-center gap-2"
        >
          <svg 
            xmlns="http://www.w3.org/2000/svg" 
            width="24" 
            height="24" 
            viewBox="0 0 24 24" 
            fill="none" 
            stroke="currentColor" 
            strokeWidth="3" 
            strokeLinecap="round" 
            strokeLinejoin="round"
          >
            <path d="M19 12H5M12 19l-7-7 7-7"/>
          </svg>
          Back to Home
        </button>

        {!coinData ? (
          <div className="flex justify-center items-center min-h-[50vh]">
            <TailSpin className="mx-auto my-12"/>
          </div>
        ) : (
          <div className="bg-white/10 backdrop-blur-lg rounded-lg p-8 shadow-lg">
            <div className="flex items-center gap-4 mb-8">
              <img
                src={coinData.icon}
                alt={coinData.name}
                className="w-16 h-16"
              />
              <div>
                <h1 className="text-3xl font-bold text-white">
                  {coinData.name} ({coinData.code})
                </h1>
                <p className="text-gray-300 text-xl">
                  Current Price: $<AnimatedPrice price={coinData.lastPrice} />
                </p>
              </div>
            </div>

            <div className="grid grid-cols-2 gap-6">
              <button
                onClick={() => openTradeModal('buy')}
                className="bg-teal-500/80 hover:bg-teal-500 text-white font-bold py-4 px-6 rounded-lg transition-colors"
              >
                Buy
              </button>
              <button
                onClick={() => openTradeModal('sell')}
                className="bg-indigo-500/80 hover:bg-indigo-500 text-white font-bold py-4 px-6 rounded-lg transition-colors"
              >
                Sell
              </button>
            </div>
          </div>
        )}

        <TradeModal
          isOpen={isTradeModalOpen}
          onClose={() => setIsTradeModalOpen(false)}
          onSubmit={handleTrade}
          type={tradeType}
          coinCode={coinData?.code || ''}
          currentPrice={coinData?.lastPrice || 0}
        />
      </div>
    </div>
  )
}

export default Coin 