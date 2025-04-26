import { TickerData } from "./TickerData"

export type TopCryptoContextType = {
    ws: WebSocket | null
    prices: Record<string, TickerData>
}