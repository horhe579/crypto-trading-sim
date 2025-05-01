import { useState, useCallback, forwardRef, useImperativeHandle } from 'react'
import Notification from './Notification'

interface NotificationItem {
  id: number
  message: string
  type: 'error' | 'success' | 'info' | 'warning'
  duration?: number
}

export interface NotificationManagerRef {
  addNotification: (message: string, type: NotificationItem['type'], duration?: number) => void
}

const NotificationManager = forwardRef<NotificationManagerRef>((_, ref) => {
  const [notifications, setNotifications] = useState<NotificationItem[]>([])
  const [nextId, setNextId] = useState(1)

  const addNotification = useCallback((message: string, type: NotificationItem['type'] = 'error', duration: number = 5000) => {
    const id = nextId
    setNextId(prev => prev + 1)
    setNotifications(prev => [{ id, message, type, duration }, ...prev])
  }, [nextId])

  const removeNotification = useCallback((id: number) => {
    setNotifications(prev => prev.filter(n => n.id !== id))
  }, [])

  useImperativeHandle(ref, () => ({
    addNotification
  }), [addNotification])

  return (
    <div className="fixed top-4 left-4 z-50">
      <div className="flex flex-col gap-2">
        {notifications.map((notification) => (
          <div
            key={notification.id}
            className="relative"
          >
            <Notification
              message={notification.message}
              type={notification.type}
              duration={notification.duration}
              onClose={() => removeNotification(notification.id)}
            />
          </div>
        ))}
      </div>
    </div>
  )
})

NotificationManager.displayName = 'NotificationManager'

export default NotificationManager 