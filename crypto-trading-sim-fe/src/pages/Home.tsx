import { useContext } from "react"
import Table from "../components/Table"
import { CryptoTickerContext } from "../contexts/CryptoTickerContext"
import { CoinData } from "../types/CoinData"
import { CoinMetadata } from "../types/CoinMetadata"
import { MarketCapContext } from "../contexts/MarketCapContext"
import AnimatedPrice from "../components/AnimatedPrice"


const Home = () => {
  const tickerContext = useContext(CryptoTickerContext);
  const marketCapContext = useContext(MarketCapContext);

  const columns = [
    "Name",
    "Price",
    "Highest Bid",
    "Lowest Ask"
  ]

  const topCoins: CoinMetadata[] = marketCapContext?.topCoins ?? [];

  const data: CoinData[] = tickerContext?.ticks ? Object.entries(tickerContext.ticks)
    .map(([, value]) => {
      const topCoinIndex = topCoins.findIndex(item => item.code === value.coinCode)
      return {...value, code: value.coinCode, name: topCoins[topCoinIndex]?.name, marketCapRank: topCoins[topCoinIndex]?.marketCapRank, icon: topCoins[topCoinIndex]?.icon}; 
    })
    .sort((a, b) => (a.marketCapRank ?? Infinity) - (b.marketCapRank ?? Infinity)) : []

  console.log("data", data)

  const mapToRow = (coin: CoinData, index: number) => (
    <tr
      key={index}
      className={`${index % 2 === 0 ? "duration-100 bg-gray-100/25 hover:bg-white/50" : "duration-100 bg-gray-400/25 hover:bg-white/50"}`}
    >
      <td
        className={`p-5 ${index === data.length - 1 ? "rounded-bl-lg" : ""}`}
      >
        <img className="inline-block px-2" width="60px" height="60px" src={coin.icon} />
        {coin.name + " - " + coin.code}
      </td>
      <td className="p-5 text-center">
        <AnimatedPrice price={coin.lastPrice} />
      </td>
      <td className="p-5 text-center font-mono">
        <AnimatedPrice price={coin.highestBid} />
      </td>
      <td
        className={`p-5 text-right font-mono ${
          index === data.length - 1 ? "rounded-br-lg" : ""
        }`}
      >
        <AnimatedPrice price={coin.lowestAsk} />
      </td>
    </tr>
  )
  

  return (
    <div className="min-h-screen p-8">
        <h1 className="py-10 text-white text-4xl font-bold mb-6 text-center">Crypto Trading Simulator</h1>
        <Table columns={columns} data={data.length ? data : null} mapToRow={mapToRow}/>
    </div>
  )
}

export default Home