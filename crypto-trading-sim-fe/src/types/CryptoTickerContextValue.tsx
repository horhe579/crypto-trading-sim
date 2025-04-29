import { TickerData } from "./TickerData"

export type CryptoTickerContextValue = {
    ws: WebSocket | null
    ticks: Record<string, TickerData>
}