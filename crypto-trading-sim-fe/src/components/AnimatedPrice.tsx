import { useEffect, useState, useRef } from 'react'
import { motion } from 'framer-motion'
import { AnimatedPriceProps } from '../types/AnimatedPriceProps'

const AnimatedPrice = ({ price, className = '' }: AnimatedPriceProps) => {
  const [previousPrice, setPreviousPrice] = useState(price)
  const [isAnimating, setIsAnimating] = useState(false)
  const [isPriceUp, setIsPriceUp] = useState(true)
  const [textColor, setTextColor] = useState('#000000')
  const spanRef = useRef<HTMLSpanElement>(null)

  useEffect(() => {
    if (spanRef.current) {
      setTextColor(getComputedStyle(spanRef.current).color)
    }
  }, [className])

  useEffect(() => {
    if (price !== previousPrice) {
      setIsPriceUp(price > previousPrice)
      setIsAnimating(false)
      setTimeout(() => {
        setIsAnimating(true)
        setPreviousPrice(price)
      }, 50)
    }
  }, [price, previousPrice])

  return (
    <motion.span
      ref={spanRef}
      className={`${className} inline-block`}
      style={{ transformOrigin: 'center' }}
      animate={isAnimating ? {
        scale: [1, 1.1, 1],
        color: ['#ffffff', isPriceUp ? '#4ade80' : '#ef4444', textColor]
      } : {}}
      transition={{
        duration: 1,
        ease: "easeInOut"
      }}
      onAnimationComplete={() => setIsAnimating(false)}
    >
      {price}
    </motion.span>
  )
}

export default AnimatedPrice 