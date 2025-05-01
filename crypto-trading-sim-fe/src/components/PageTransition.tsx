import { motion } from "framer-motion"
import { useLocation } from "react-router-dom"
import { useEffect, useState } from "react"

interface PageTransitionProps {
  children: React.ReactNode
}

const PageTransition = ({ children }: PageTransitionProps) => {
  const location = useLocation()
  const [direction, setDirection] = useState(1)

  // logic for determining swipe direction
  useEffect(() => {
    if (location.pathname === "/portfolio") {
      setDirection(-1) 
    } else {
      setDirection(1)
    }
  }, [location.pathname])

  return (
    <motion.div
      initial={{ opacity: 0, x: direction * 100 }}
      animate={{ opacity: 1, x: 0 }}
      exit={{ opacity: 0, x: -direction * 100 }}
      transition={{ duration: 0.3, ease: "easeInOut" }}
    >
      {children}
    </motion.div>
  )
}

export default PageTransition 