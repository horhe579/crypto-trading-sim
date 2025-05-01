import { createContext, useState, ReactNode } from "react";
import { OrderContextType } from "../types/OrderContextType";
import { GenericChildrenProps } from "../types/GenericChildrenProps";

export const OrderContext = createContext<OrderContextType | null>(null);

export const OrderProvider = ({ children }: GenericChildrenProps) => {
  const [orderCount, setOrderCount] = useState(0);

  const incrementOrderCount = () => {
    setOrderCount(prev => prev + 1);
  };

  return (
    <OrderContext.Provider value={{ orderCount, incrementOrderCount }}>
      {children}
    </OrderContext.Provider>
  );
};