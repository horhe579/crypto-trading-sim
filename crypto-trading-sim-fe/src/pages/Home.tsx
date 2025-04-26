import { useContext } from "react"
import Table from "../components/Table"
import { TickerData } from "../types/TickerData"
import { TopCryptoContext } from "../contexts/TopCryptoContext"

const Home = () => {
  const context = useContext(TopCryptoContext);

  const columns = [
    "Name",
    "Symbol", 
    "Highest Bid",
    "Lowest Ask"
  ]

  const data = context?.prices ? Object.entries(context.prices).map(([, value]) => {
    return value; 
  }) : []

  const mapToRow = (coin: TickerData, index: number) => (
    <tr key={index} className={index % 2 === 0 ? "bg-gray-900 bg-opacity-40" : "bg-gray-800 bg-opacity-40"}>
        <td className="border border-gray-700 px-5 py-5">{coin.name}</td>
        <td className="border border-gray-700 px-5 py-5 text-center">{coin.symbol}</td>
        <td className="border border-gray-700 px-5 py-5 text-right font-mono">{coin.highestBid}</td>
        <td className="border border-gray-700 px-5 py-5 text-right font-mono">{coin.lowestAsk}</td>
    </tr>
  )

  return (
    <div>
        <h1 className="text-white text-4xl font-bold mb-6 text-center">Crypto Trading Simulator</h1>
        <Table columns={columns} data={data.length ? data : null} mapToRow={mapToRow}/>
    </div>
  )
}

export default Home