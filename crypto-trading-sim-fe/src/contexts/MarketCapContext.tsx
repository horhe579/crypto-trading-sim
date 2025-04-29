import { createContext, useEffect, useState } from "react"
import { BaseProviderProps } from "../types/BaseProviderProps"
import { Currency } from "../types/Currency"
import { CoinMetadata } from "../types/CoinMetadata"
import { getErrorMessage } from "../util/Util"
import { MarketCapContextValue } from "../types/MarketCapContextValue"

export const MarketCapContext = createContext<MarketCapContextValue | null>(null)

export const MarketCapProvider = ({ children }: BaseProviderProps) => {

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

    const buildQueryParams = (pageLimit?: number):URLSearchParams => {
        const validPageLimit = pageLimit && pageLimit > 0 ? pageLimit : 20

        const queryParams = new URLSearchParams()
        queryParams.append("vs_currency", currency.name)
        queryParams.append("per_page", Math.round(validPageLimit).toString())

        return queryParams
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
        console.error("API Error:", errorMessage);
        setError(errorMessage);
        setIsLoading(false);
    }
    
    const fetchCoins = (pageLimit?: number) => {
        const apiKey = import.meta.env.VITE_COINGECKO_API_KEY
        
        setIsLoading(true);
        setError(null);
        
        const options = {
            method: 'GET',
            headers: {accept: 'application/json', 'x-cg-api-key': apiKey}
        }
          
        fetch('https://cors-anywhere.herokuapp.com/api.coingecko.com/api/v3/coins/markets?' + buildQueryParams(pageLimit), options)
            .then(response => {
                if (!response.ok) throw new Error(`HTTP error: ${response.status}`);
                return response.json();
            })
            .then(handleSuccess)
            .catch(handleError)
    }

    useEffect(() => {
        fetchCoins()
    }, [currency])

    return (
    <MarketCapContext.Provider value={{currency, topCoins, isLoading, error}}>
        {children}
    </MarketCapContext.Provider>
    )
}
