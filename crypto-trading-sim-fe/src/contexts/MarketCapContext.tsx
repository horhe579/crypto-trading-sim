import { createContext, useEffect, useState } from "react"
import { BaseProviderProps } from "../types/BaseProviderProps"
import { Currency } from "../types/Currency"

export const MarketCapContext = createContext(null)

export const MarketCapProvider = ({ children }: BaseProviderProps) => {
     
    const contextValue = {}

    const [currency, setCurrency] = useState<Currency>({
        name: "usd",
        symbol: "$"
    });
    const [topCoins, setTopCoins] = useState([])

    const fetchCoins = (currency: string, pageLimit?: number) => {
        const apiKey = import.meta.env.VITE_COINGECKO_API_KEY

        const validPageLimit = pageLimit && pageLimit > 0 ? pageLimit : 20

        console.log(currency)

        const queryParams = new URLSearchParams()
        queryParams.append("vs_currency", currency)
        queryParams.append("per_page", Math.round(validPageLimit).toString())
        
        const options = {
            method: 'GET',
            headers: {accept: 'application/json', 'x-cg-api-key': apiKey}
        }
          
        fetch('https://cors-anywhere.herokuapp.com/api.coingecko.com/api/v3/coins/markets?' + queryParams, options)
            .then(res => res.json())
            .then(res => console.log(res))
            .catch(err => console.error(err))
    }

    useEffect(() => {
        fetchCoins(currency.name)
    }, [currency])

    return (
    <MarketCapContext.Provider value={null}>
        {children}
    </MarketCapContext.Provider>
    )
}
