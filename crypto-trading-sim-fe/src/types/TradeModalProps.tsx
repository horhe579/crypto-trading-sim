export type TradeModalProps = {
    isOpen: boolean
    onClose: () => void
    onSubmit: (amount: number) => void
    type: 'buy' | 'sell'
    coinCode: string
    currentPrice: number
  }