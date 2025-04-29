import { TableProps } from "../types/TableProps"
import { TailSpin } from 'react-loading-icons'

function Table<T>({columns, data, mapToRow}: TableProps<T>) {
  return (
    <div className="w-4/5 mx-auto">
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

            <tbody>
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