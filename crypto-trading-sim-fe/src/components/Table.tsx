interface Props<T>{
    columns: string[],
    data: T[] | null,
    mapToRow: (item: T, index: number) => React.ReactNode
}

function Table<T>({columns, data, mapToRow}: Props<T>) {
  return (
    <div className="w-3/5 mx-auto">
        <table className={`w-full border-collapse text-white table-auto ${data ? 'opacity-100 transition-opacity duration-600 ease-out' : 'opacity-0'}`}>
            <thead>
                <tr>
                    {columns.map((column, index) => (
                        <th 
                            key={index} 
                            className={`w-1/${columns.length} px-5 py-3 ${
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
          <div className="text-center font-bold text-xl text-white mt-20">Loading data...</div>
        )}
    </div>
  )
}

export default Table