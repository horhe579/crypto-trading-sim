import { CryptoTickerProvider } from "./contexts/CryptoTickerContext"
import { MarketCapProvider } from "./contexts/MarketCapContext"
import Home from "./pages/Home"

const App = () => {
  return (
    <MarketCapProvider>
      <CryptoTickerProvider>
        <div className="bg-gradient-to-b from-indigo-900 to-[#0d064d] min-h-screen w-full">
          <Home/>
        </div>
      </CryptoTickerProvider>
    </MarketCapProvider>
  )
}

export default App