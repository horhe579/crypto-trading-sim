import { createContext, useContext, useEffect, useState } from 'react'
import { GenericChildrenProps } from '../types/GenericChildrenProps'
import { UserContextValue } from '../types/UserContextValue'

export const UserContext = createContext<UserContextValue | null>(null)

export const UserProvider = ({ children }: GenericChildrenProps) => {
  const [userId, setUserId] = useState<string>('')

  useEffect(() => {
    const storedUserId = localStorage.getItem('userId')
    if (storedUserId) {
      setUserId(storedUserId)
    } else {
        // if not exists gen new uuid, works only on local pages
      const newUserId = crypto.randomUUID()
      localStorage.setItem('userId', newUserId)
      setUserId(newUserId)
    }
  }, [])

  return (
    <UserContext.Provider value={{ userId }}>
      {children}
    </UserContext.Provider>
  )
} 