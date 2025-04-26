import Table from "../components/Table";
import { Cryptocurrency } from "../types/Cryptocurrency";

const Home = () => {
  const columns = [
    "Name",
    "Symbol", 
    "Highest Bid",
    "Lowest Ask"
  ]
  const cryptocurrencies = [
    { name: "Bitcoin", symbol: "BTC", highestBid: "$27,350.00", lowestAsk: "$27,365.50" },
    { name: "Ethereum", symbol: "ETH", highestBid: "$1,847.25", lowestAsk: "$1,848.80" },
    { name: "Binance Coin", symbol: "BNB", highestBid: "$563.77", lowestAsk: "$564.15" },
    { name: "Solana", symbol: "SOL", highestBid: "$135.62", lowestAsk: "$135.88" },
    { name: "Ripple", symbol: "XRP", highestBid: "$0.5294", lowestAsk: "$0.5301" },
    { name: "Cardano", symbol: "ADA", highestBid: "$0.4521", lowestAsk: "$0.4532" },
    { name: "Dogecoin", symbol: "DOGE", highestBid: "$0.0827", lowestAsk: "$0.0829" },
    { name: "Polkadot", symbol: "DOT", highestBid: "$6.72", lowestAsk: "$6.74" },
    { name: "Polygon", symbol: "MATIC", highestBid: "$0.8345", lowestAsk: "$0.8352" },
    { name: "Litecoin", symbol: "LTC", highestBid: "$75.21", lowestAsk: "$75.35" },
    { name: "Chainlink", symbol: "LINK", highestBid: "$13.78", lowestAsk: "$13.81" },
    { name: "Avalanche", symbol: "AVAX", highestBid: "$27.88", lowestAsk: "$27.93" },
    { name: "Uniswap", symbol: "UNI", highestBid: "$6.92", lowestAsk: "$6.94" },
    { name: "Cosmos", symbol: "ATOM", highestBid: "$8.76", lowestAsk: "$8.78" },
    { name: "Stellar", symbol: "XLM", highestBid: "$0.1145", lowestAsk: "$0.1149" },
    { name: "Tron", symbol: "TRX", highestBid: "$0.1235", lowestAsk: "$0.1238" },
    { name: "Algorand", symbol: "ALGO", highestBid: "$0.1687", lowestAsk: "$0.1692" },
    { name: "VeChain", symbol: "VET", highestBid: "$0.0315", lowestAsk: "$0.0317" },
    { name: "Filecoin", symbol: "FIL", highestBid: "$4.91", lowestAsk: "$4.93" },
    { name: "Tezos", symbol: "XTZ", highestBid: "$0.9782", lowestAsk: "$0.9795" }
  ];

  const mapToRow = (coin: Cryptocurrency, index: number) => (
    <tr key={index} className={index % 2 === 0 ? "bg-gray-900 bg-opacity-40" : "bg-gray-800 bg-opacity-40"}>
        <td className="border border-gray-700 px-5 py-5">{coin.name}</td>
        <td className="border border-gray-700 px-5 py-5 text-center">{coin.symbol}</td>
        <td className="border border-gray-700 px-5 py-5 text-right font-mono">{coin.highestBid}</td>
        <td className="border border-gray-700 px-5 py-5 text-right font-mono">{coin.lowestAsk}</td>
    </tr>
  )
  return (
    <div>
      <div className="min-h-screen flex flex-col items-center pt-4 pb-6">
        <h1 className="text-white text-4xl font-bold mb-6">Crypto Trading Simulator</h1>
            <Table columns={columns} data={cryptocurrencies} mapToRow={mapToRow}/>
        </div>
    </div>
  )
}

export default Home