
class Table {
    id: number;
    numberInLounge: number;
    tableDefinition: TableDefinition;

    public constructor(id: number, numberInLounge: number, tableDefinition: TableDefinition) {
        this.id = id;
        this.numberInLounge = numberInLounge;
        this.tableDefinition = tableDefinition;
    }

}

class TableDefinition {
    categoryName: string;
    standaloneCapacity: number;

    public constructor(categoryName: string, standaloneCapacity: number) {
        this.categoryName = categoryName;
        this.standaloneCapacity = standaloneCapacity;
    }
}
export default Table;