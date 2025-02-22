// @ts-ignore

import Table from "./Table.ts";
import '@material/web/divider/divider.js';
interface TableCardProps {
    table?: Table
}

function TableCard(table: TableCardProps) {

    return (
        <>
            <div className="card col shadow-sm" style={{width: "8rem", padding: 0}} >
                <div className="card-body" style={{padding: 0}}>
                    <h5 className="card-title" style={{padding: "2rem"}}>{table.table?.numberInLounge}</h5>
                    <h6 className="card-subtitle mb-2 text-body-secondary">
                        {
                            table
                                .table
                                ?.tableDefinition
                                .categoryName
                        }
                    </h6>
                    <h6 className="card-subtitle mb-2 text-body-secondary">
                        {
                            "Capacity: " + table.table?.tableDefinition.standaloneCapacity
                        }
                    </h6>
                    <md-divider />
                    <p></p>
                </div>
            </div>
        </>
    )
}

export default TableCard;