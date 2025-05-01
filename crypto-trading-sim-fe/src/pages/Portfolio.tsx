import { useContext, useEffect, useState } from 'react'
import { HoldingResponse } from '../types/HoldingResponse'
import { getErrorMessage } from '../util/Util'
import { UserContext } from '../contexts/UserContext'
import Holding from '../components/Holding'

const Portfolio = () => {
  const [holdings, setHoldings] = useState<HoldingResponse[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const userContext = useContext(UserContext)

  useEffect(() => {
    const fetchHoldings = async () => {
      if (!userContext?.userId) return

      try {
        const response = await fetch(`${import.meta.env.VITE_BACKEND_SERVICE_URL}/holdings`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'X-User-Id': userContext.userId
          }
        })

        if (!response.ok) {
          throw new Error('Failed to fetch holdings')
        }
        const data = await response.json()
        setHoldings(data)
      } catch (err) {
        setError(getErrorMessage(err))
      } finally {
        setLoading(false)
      }
    }

    fetchHoldings()
  }, [userContext])

  if (loading) {
    return <div className="flex justify-center items-center h-screen">Loading...</div>
  }

  if (error) {
    return <div className="flex justify-center items-center h-screen text-red-500">{error}</div>
  }

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-2xl font-bold mb-6 text-white">Your Portfolio</h1>
      <div className="space-y-4">
        {holdings.map((holding) => (
          <Holding key={holding.cryptocurrencySymbol} holding={holding} />
        ))}
      </div>
    </div>
  )
}

export default Portfolio 