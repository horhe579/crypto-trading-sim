import { useState } from 'react'

interface TradeModalProps {
  isOpen: boolean
  onClose: () => void
  onSubmit: (amount: number) => void
  type: 'buy' | 'sell'
  coinCode: string
  currentPrice: number
}

const TradeModal = ({ isOpen, onClose, onSubmit, type, coinCode, currentPrice }: TradeModalProps) => {
  const [amount, setAmount] = useState<string>('1')

  if (!isOpen) return null

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    const numericAmount = parseFloat(amount)
    if (!isNaN(numericAmount) && numericAmount > 0) {
      onSubmit(numericAmount)
      onClose()
    }
  }

  const totalCost = parseFloat(amount) * currentPrice

  return (
    <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
      <div className="bg-white/10 backdrop-blur-lg rounded-lg p-6 w-96">
        <h2 className="text-xl font-bold text-white mb-4">
          {type === 'buy' ? 'Buy' : 'Sell'} {coinCode}
        </h2>
        <form onSubmit={handleSubmit}>
          <div className="mb-4">
            <label className="block text-gray-300 mb-2">Amount</label>
            <input
              type="number"
              value={amount}
              onChange={(e) => setAmount(e.target.value)}
              className="w-full bg-white/5 border border-white/10 rounded px-3 py-2 text-white focus:outline-none focus:border-indigo-500"
              step="0.00000001"
              min="0.00000001"
            />
          </div>
          <div className="text-gray-300 mb-4">
            Total Cost: ${totalCost.toFixed(2)}
          </div>
          <div className="flex gap-4">
            <button
              type="button"
              onClick={onClose}
              className="flex-1 bg-gray-500/50 hover:bg-gray-500 text-white font-bold py-2 px-4 rounded transition-colors"
            >
              Cancel
            </button>
            <button
              type="submit"
              className={`flex-1 ${
                type === 'buy' 
                  ? 'bg-teal-500/80 hover:bg-teal-500' 
                  : 'bg-indigo-500/80 hover:bg-indigo-500'
              } text-white font-bold py-2 px-4 rounded transition-colors`}
            >
              {type === 'buy' ? 'Buy' : 'Sell'}
            </button>
          </div>
        </form>
      </div>
    </div>
  )
}

export default TradeModal 