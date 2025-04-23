class TableDefinition {
    categoryName: string;
    standaloneCapacity: number;

    public constructor(categoryName: string, standaloneCapacity: number) {
        this.categoryName = categoryName;
        this.standaloneCapacity = standaloneCapacity;
    }
}

export default TableDefinition;