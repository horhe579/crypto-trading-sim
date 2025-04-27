import { TickerData } from "./TickerData"

export type CryptoTickerContextType = {
    ws: WebSocket | null
    ticks: Record<string, TickerData>
}