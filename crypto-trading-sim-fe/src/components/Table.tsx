// headers: []; data: []
interface Props<T>{
    columns: string[],
    data: T[],
    mapToRow: (item: T, index: number) => React.ReactNode,
    styles?: {
        header?: string,
        row?: string,
        cell?: string
    }
}

function Table<T>({columns, data, mapToRow}: Props<T>) {
  return (
    <div className="w-3/5 mx-auto">
        <table className="w-full border-collapse text-white table-auto">
            <thead>
                <tr>
                    {columns.map((column, index) => (
                        <th key={index} className={`px-5 pt-4 ${index === 0 ? 'text-left w-1/6' : 
                            index === 1 ? 'text-center w-1/4' : 
                            'text-right w-1/4'}`}>
                            {column}
                        </th>
                    ))}
                </tr>
            </thead>
            <tbody>
                {data.map((item, index) => mapToRow(item, index))}
            </tbody>
        </table>
    </div>
  )
}

export default Table