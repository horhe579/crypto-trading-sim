import { TableProps } from "../types/TableProps"
import { TailSpin } from 'react-loading-icons'
import { useNavigate } from "react-router-dom"

function Table<T>({columns, data, mapToRow}: TableProps<T>) {
  const navigate = useNavigate()

  const handleRowClick = (item: any) => {
    if (item.code) {
      navigate(`/coin/${item.code.toLowerCase()}`)
    }
  }

  return (
    <div className="w-4/5 mx-auto mb-20">
        <table className={`w-full border-collapse text-gray-900 table-auto ${data ? 'rounded-xl bg-white/25 opacity-100 transition-opacity duration-600 ease-out' : 'opacity-0'}`}>
            <thead className="text-gray-300">
                <tr>
                    {columns.map((column, index) => (
                        <th 
                            key={index} 
                            className={`w-1/${columns.length} p-5 ${
                                index === 0 ? 'text-left' : 
                                index === columns.length - 1 ? 'text-right' : 
                                'text-center'
                            }`}
                        >
                            {column}
                        </th>
                    ))}
                </tr>
            </thead>

            <tbody onClick={(e) => {
              const row = (e.target as HTMLElement).closest('tr')
              if (row) {
                const index = Array.from(row.parentElement?.children || []).indexOf(row)
                if (data && index >= 0) {
                  handleRowClick(data[index])
                }
              }
            }}>
                {data?.map((item, index) => mapToRow(item, index))}
            </tbody>
        </table>
        
        {!data && (
          <TailSpin className="mx-auto my-12"/>
        )}
    </div>
  )
}

export default Table