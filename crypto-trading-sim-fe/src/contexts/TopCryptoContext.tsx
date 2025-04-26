import { createContext, useEffect, useState } from "react"
import { TopCryptoContextType } from "../types/TopCryptoContextType"
import { BaseProviderProps } from "../types/BaseProviderProps"
import { TickerData } from "../types/TickerData"

export const TopCryptoContext = createContext<TopCryptoContextType | null>(null)

export const TopCryptoProvider = ({ children }: BaseProviderProps) => {

    const topCoins = [
        "BTC/USD",
        "ETH/USD",
        "USDT/USD",
        "BNB/USD",
        "SOL/USD",
        "XRP/USD",
        "ADA/USD",
        "DOGE/USD",
        "TRX/USD",
        "MATIC/USD",
        "DOT/USD",
        "LTC/USD",
        "AVAX/USD",
        "SHIB/USD",
        "TON/USD",
        "BCH/USD",
        "LINK/USD",
        "XLM/USD",
        "ICP/USD",
        "ATOM/USD"
    ]
          

    const [ws, setWs] = useState<WebSocket | null>(null)
    const [prices, setPrices] = useState<Record<string, TickerData>>({})

    const mapToTickerData = (message: any): TickerData => {
        const data = message.data[0];
        const coinCode = data.symbol.split('/')[0];
         
        return {
            name: coinCode,
            symbol: coinCode,
            highestBid: data.bid,
            lowestAsk: data.ask
        };
    }

    const handleSnapshot = (tickerData: TickerData) => {
        console.log("Processing snapshot data ")
        console.log(tickerData)
        setPrices(prev => ({
            ...prev,
            [tickerData.name]: tickerData
        }))
    }

    const handleUpdate = (tickerData: TickerData) => {
        console.log("Processing update data ")
        console.log(tickerData)
        setPrices(prev => ({
            ...prev,
            [tickerData.name]: tickerData
        }))
    }


    const processWebSocketMessage = (message: any) => {
        if(message?.channel === "ticker" && Array.isArray(message.data)){
            const messageType = message.type;
            const tickerData = mapToTickerData(message);

            switch (messageType) {
                case "snapshot": {
                    handleSnapshot(tickerData)
                    break
                }
                case "update": {
                    handleUpdate(tickerData)
                    break
                }
                default: {
                    console.log(`Unknown message type: ${messageType}`)
                    break
                }
            }
            console.log(message)
        }
    }

    const buildWebsocketSubscribeMessage = (currencyPairs: string[]) => {
        return JSON.stringify({
            method: "subscribe",
            params: {
                channel: "ticker",
                symbol: currencyPairs
            }})
    }

    useEffect(() => {
        const websocket = new WebSocket("wss://ws.kraken.com/v2")
        setWs(websocket)

        websocket.onopen = () => {
            console.log('Kraken ws connection established.')
            websocket.send(buildWebsocketSubscribeMessage(topCoins))
        }

        websocket.onmessage = (event) => {
            const message = JSON.parse(event.data)
            processWebSocketMessage(message)
        } 

        return () => {
            websocket.close();
        };

    }, [])

  return (
    <TopCryptoContext.Provider value={{ ws, prices }}>
        {children}
    </TopCryptoContext.Provider>
  )
}
