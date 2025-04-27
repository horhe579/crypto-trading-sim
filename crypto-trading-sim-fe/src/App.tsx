import { CryptoTickerProvider } from "./contexts/CryptoTickerContext"
import { MarketCapProvider } from "./contexts/MarketCapContext"
import Home from "./pages/Home"

const App = () => {
  return (
    <MarketCapProvider>
      <CryptoTickerProvider>
        <div className="bg-gradient-to-b from-blue-800 to-purple-900 min-h-screen w-full">
          <Home/>
        </div>
      </CryptoTickerProvider>
    </MarketCapProvider>
  )
}

export default App