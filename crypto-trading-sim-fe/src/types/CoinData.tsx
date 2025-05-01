import { CoinMetadata } from "./CoinMetadata"
import { TickerData } from "./TickerData"

// merged data from tickers and metadata(image, full name, etc.)
export type CoinData = CoinMetadata & TickerData