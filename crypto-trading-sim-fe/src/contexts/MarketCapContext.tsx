import { createContext, useEffect, useState } from "react"
import { GenericChildrenProps } from "../types/GenericChildrenProps"
import { Currency } from "../types/Currency"
import { CoinMetadata } from "../types/CoinMetadata"
import { getErrorMessage } from "../util/Util"
import { MarketCapContextValue } from "../types/MarketCapContextValue"

export const MarketCapContext = createContext<MarketCapContextValue | null>(null)

export const MarketCapProvider = ({ children }: GenericChildrenProps) => {
    const [currency, setCurrency] = useState<Currency>({
        name: "usd",
        symbol: "$"
    });
    const [topCoins, setTopCoins] = useState<CoinMetadata[]>([])
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const mapToCoinMetadata = (response: any): CoinMetadata => {
        return {
            name: response.name,
            code: response.symbol.toUpperCase(), 
            icon: response.image,
            marketCapRank: response.market_cap_rank
        };
    }

    const handleSuccess = (data: any) => {
        if(Array.isArray(data)){
            console.log(data)
            setTopCoins(data.map(mapToCoinMetadata));
            setIsLoading(false);
        }
    }

    const handleError = (error: any) => {
        const errorMessage = getErrorMessage(error)
        console.error("Error processing data:", errorMessage);
        setError(errorMessage);
        setIsLoading(false);
    }
    
    const loadCoins = () => {
        setIsLoading(true);
        setError(null);
        
        fetch('/src/util/coigecko-topcoins.txt')
            .then(response => response.text())
            .then(text => {
                try {
                    const coins = JSON.parse(text);
                    handleSuccess(coins);
                } catch (error) {
                    handleError(error);
                }
            });
    }

    useEffect(() => {
        loadCoins();
    }, [currency])

    return (
    <MarketCapContext.Provider value={{currency, topCoins, isLoading, error}}>
        {children}
    </MarketCapContext.Provider>
    )
}
