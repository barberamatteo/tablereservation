import Table from "../model/Table.ts";
import Reservation from "../model/Reservation.ts";
import '@material/web/divider/divider.js';
interface TableCardProps {
    table?: Table,
    reservations?: Reservation[]
}

function TableCard(props: TableCardProps) {

    return (
        <>
            <div className="card col shadow-sm" style={{width: "8rem", padding: 0}} >
                <div className="card-body" style={{padding: 0}}>
                    <h5 className="card-title" style={{padding: "2rem"}}>{props.table?.numberInLounge}</h5>
                    <h6 className="card-subtitle mb-2 text-body-secondary">
                        {
                            props
                                .table
                                ?.tableDefinition
                                .categoryName
                        }
                    </h6>
                    <h6 className="card-subtitle mb-2 text-body-secondary">
                        {
                            "Capacity: " + props.table?.tableDefinition.standaloneCapacity
                        }
                    </h6>
                    <md-divider />
                    <div>
                        {
                            props.reservations?.map((reservation: Reservation, index: number) => (
                                <p>{reservation.customer.name + "(x" + reservation.numberOfPeople + ")"}</p>
                            ))
                        }
                    </div>
                </div>
            </div>
        </>
    )
}

export default TableCard;