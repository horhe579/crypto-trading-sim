import { Link, useLocation } from "react-router-dom"
import { useContext, useEffect, useState } from "react"
import { UserContext } from "../contexts/UserContext"
import { getErrorMessage } from "../util/Util"
import AnimatedPrice from "./AnimatedPrice"
import { OrderContext } from "../contexts/OrderContext"
const DEFAULT_USER_BALANCE = 10000

interface UserResponse {
  balance: number
}

const Navbar = () => {
  const location = useLocation()
  const userContext = useContext(UserContext)
  const orderContext = useContext(OrderContext)
  const [userAmount, setUserAmount] = useState<number>()

  const isActive = (path: string) => {
    return location.pathname === path ? "text-white" : "text-gray-400 hover:text-white"
  }

  const fetchUserAmount = async () => {
    if (!userContext?.userId) return

    try {
      const response = await fetch(`${import.meta.env.VITE_BACKEND_SERVICE_URL}/users`, {
        headers: {
          'X-User-Id': userContext.userId
        }
      })

      if (!response.ok) {
        if (response.status === 404) {
          return
        }
        const errorData = await response.json()
        console.error('Failed to fetch user amount:', errorData)
        return
      }

      const data: UserResponse = await response.json()
      setUserAmount(data.balance)
    } catch (error) {
      console.error('Error fetching user amount:', error)
    }
  }

  useEffect(() => {
    fetchUserAmount()
  }, [userContext?.userId, orderContext?.orderCount])

  const handleReset = async () => {
    if (!userContext?.userId) return

    try {
      const response = await fetch(`${import.meta.env.VITE_BACKEND_SERVICE_URL}/users/reset`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'X-User-Id': userContext.userId
        }
      })

      if (!response.ok) {
        const errorData = await response.json()
        console.error('Reset failed:', errorData)
        getErrorMessage(errorData) || 'Failed to reset account'
      }

      // refresh to apply the changes
      window.location.reload()
    } catch (error) {
      console.error('Error resetting account:', error)
    }
  }

  return (
    <nav className="bg-indigo-900/50 p-4">
      <div className="container mx-auto flex justify-between items-center">
        <div className="flex space-x-8">
          <Link 
            to="/" 
            className={`${isActive("/")} transition-colors duration-200`}
          >
            Home
          </Link>
          <Link 
            to="/portfolio" 
            className={`${isActive("/portfolio")} transition-colors duration-200`}
          >
            Portfolio
          </Link>
        </div>
        <div className="flex items-center gap-6">
          <div className="text-white">
            <span className="text-gray-400 mr-2">Balance:</span>
            $<AnimatedPrice price={userAmount || DEFAULT_USER_BALANCE} />
          </div>
          <button
            onClick={handleReset}
            className="bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded transition-colors duration-200"
          >
            Reset Account
          </button>
        </div>
      </div>
    </nav>
  )
}

export default Navbar 