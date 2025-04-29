import { CoinMetadata } from "./CoinMetadata"
import { Currency } from "./Currency"

export type MarketCapContextValue = {
    currency: Currency,
    topCoins: CoinMetadata[],
    isLoading: boolean,
    error: string | null
}