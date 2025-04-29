import { createContext, useContext, useEffect, useState } from "react"
import { CryptoTickerContextValue } from "../types/CryptoTickerContextValue"
import { BaseProviderProps } from "../types/BaseProviderProps"
import { TickerData } from "../types/TickerData"
import { MarketCapContext } from "./MarketCapContext"

export const CryptoTickerContext = createContext<CryptoTickerContextValue | null>(null)

export const CryptoTickerProvider = ({ children }: BaseProviderProps) => {
    const context = useContext(MarketCapContext);

    const getTradingPairs = (): string[] => {
        if (context && context.topCoins && context.topCoins.length > 0) {
            return context.topCoins.map(coin => `${coin.code}/${context.currency.name.toUpperCase()}`);
        }
        
        return [
            "BTC/USD",
            "ETH/USD",
        ];
    };    

    const [ws, setWs] = useState<WebSocket | null>(null)
    const [ticks, setTicks] = useState<Record<string, TickerData>>({})

    const mapToTickerData = (message: any): TickerData => {
        const data = message.data[0];
        const coinCode = data.symbol.split('/')[0];
         
        return {
            coinCode: coinCode,
            highestBid: data.bid,
            lowestAsk: data.ask,
            lastPrice: data.last 
        };
    }

    const handleUpdate = (tickerData: TickerData) => {
        console.log("Processing update data ", tickerData)
        setTicks(prev => ({
            ...prev,
            [tickerData.coinCode]: tickerData
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
            websocket.send(buildWebsocketSubscribeMessage(getTradingPairs()))
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
