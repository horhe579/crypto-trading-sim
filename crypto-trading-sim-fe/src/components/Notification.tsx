import { useEffect, useState } from 'react'

interface NotificationProps {
  message: string
  type?: 'error' | 'success' | 'info' | 'warning'
  duration?: number
  onClose?: () => void
}

const Notification = ({ message, type = 'error', duration = 5000, onClose }: NotificationProps) => {
  const [isVisible, setIsVisible] = useState(true)
  const [isFading, setIsFading] = useState(false)

  useEffect(() => {
    if (duration === 0) return

    const fadeOutTimer = setTimeout(() => {
      setIsFading(true)
    }, duration - 500)

    const hideTimer = setTimeout(() => {
      setIsVisible(false)
      onClose?.()
    }, duration)

    return () => {
      clearTimeout(fadeOutTimer)
      clearTimeout(hideTimer)
    }
  }, [duration, onClose])

  const getBgColor = () => {
    switch (type) {
      case 'error':
        return 'bg-red-500'
      case 'success':
        return 'bg-green-500'
      case 'warning':
        return 'bg-yellow-500'
      case 'info':
        return 'bg-blue-500'
      default:
        return 'bg-red-500'
    }
  }

  if (!isVisible) return null

  return (
    <div 
      className={`w-80 p-4 rounded-lg shadow-lg transition-all duration-500 ease-in-out ${getBgColor()} text-white transform ${
        isFading ? 'opacity-0 translate-x-[-10px]' : 'opacity-100 translate-x-0'
      }`}
    >
      <div className="flex items-center justify-between gap-4">
        <span className="text-sm">{message}</span>
        <button
          onClick={() => {
            setIsFading(true)
            setTimeout(() => {
              setIsVisible(false)
              onClose?.()
            }, 500)
          }}
          className="text-white hover:text-gray-200 transition-colors"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            className="h-4 w-4"
            viewBox="0 0 20 20"
            fill="currentColor"
          >
            <path
              fillRule="evenodd"
              d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"
              clipRule="evenodd"
            />
          </svg>
        </button>
      </div>
    </div>
  )
}

export default Notification 