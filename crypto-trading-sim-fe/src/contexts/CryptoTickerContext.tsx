import { createContext, useEffect, useState } from "react"
import { CryptoTickerContextType } from "../types/CryptoTickerContextType"
import { BaseProviderProps } from "../types/BaseProviderProps"
import { TickerData } from "../types/TickerData"

export const CryptoTickerContext = createContext<CryptoTickerContextType | null>(null)

export const CryptoTickerProvider = ({ children }: BaseProviderProps) => {

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
    const [ticks, setTicks] = useState<Record<string, TickerData>>({})

    const mapToTickerData = (message: any): TickerData => {
        const data = message.data[0];
        const coinCode = data.symbol.split('/')[0];
         
        return {
            name: coinCode,
            symbol: coinCode,
            highestBid: data.bid,
            lowestAsk: data.ask,
            last: data.last
        };
    }

    const handleUpdate = (tickerData: TickerData) => {
        console.log("Processing update data ", tickerData)
        setTicks(prev => ({
            ...prev,
            [tickerData.name]: tickerData
        }))
    }

    const processWebSocketMessage = (message: any) => {
        if(message?.channel === "ticker" && Array.isArray(message.data)){
            const messageType = message.type;

            switch (messageType) {
                case "snapshot" : 
                case "update":{
                    const tickerData = mapToTickerData(message);
                    handleUpdate(tickerData)
                    break
                }
                default: {
                    console.log(`Unknown message type: ${messageType}`)
                    break
                }
            }
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
    <CryptoTickerContext.Provider value={{ ws, ticks }}>
        {children}
    </CryptoTickerContext.Provider>
  )
}
