export type TableProps<T> = {
    columns: string[],
    data: T[] | null,
    mapToRow: (item: T, index: number) => React.ReactNode
}
