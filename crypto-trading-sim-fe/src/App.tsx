import { BrowserRouter as Router, Routes, Route, useLocation } from "react-router-dom"
import { AnimatePresence } from "framer-motion"
import { CryptoTickerProvider } from "./contexts/CryptoTickerContext"
import { MarketCapProvider } from "./contexts/MarketCapContext"
import { UserProvider } from "./contexts/UserContext"
import Home from "./pages/Home"
import Portfolio from "./pages/Portfolio"
import Coin from "./pages/Coin"
import Navbar from "./components/Navbar"
import PageTransition from "./components/PageTransition"

const AnimatedRoutes = () => {
  const location = useLocation()

  return (
    <AnimatePresence mode="wait">
      <Routes location={location} key={location.pathname}>
        <Route path="/" element={
          <PageTransition>
            <Home />
          </PageTransition>
        } />
        <Route path="/portfolio" element={
          <PageTransition>
            <Portfolio />
          </PageTransition>
        } />
        <Route path="/coin/:coinCode" element={
          <PageTransition>
            <Coin />
          </PageTransition>
        } />
      </Routes>
    </AnimatePresence>
  )
}

const App = () => {
  return (
    <Router>
      <UserProvider>
        <MarketCapProvider>
          <CryptoTickerProvider>
            <div className="bg-gradient-to-b from-indigo-900 to-[#0d064d] min-h-screen w-full">
              <Navbar />
              <AnimatedRoutes />
            </div>
          </CryptoTickerProvider>
        </MarketCapProvider>
      </UserProvider>
    </Router>
  )
}

export default App