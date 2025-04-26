import { TopCryptoProvider } from "./contexts/TopCryptoContext"
import Home from "./pages/Home"

const App = () => {
  return (
    <TopCryptoProvider>
      <div className="bg-gradient-to-b from-blue-800 to-purple-900 min-h-screen w-full">
        <Home/>
      </div>
    </TopCryptoProvider>
  )
}

export default App