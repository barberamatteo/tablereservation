import TableDefinition from "./TableDefinition.ts";

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


export default Table;