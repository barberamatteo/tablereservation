import { useState, useEffect } from 'react';
import Table from "../model/Table.ts"
import Reservation from "../model/Reservation.ts"
import TableCard from "./TableCard.tsx";
async function fetchTables(): Promise<Table[]> {
    const response = await fetch('http://localhost:8080/api/v1.0.0/admin/tables/getall/');
    return await response.json();
}

async function fetchReservations(): Promise<Reservation[]>{
    const response = await fetch('http://localhost:8080/api/v1.0.0/reservation/getall/');
    return await response.json();
}

function LoungeCardHolder() {
    const [tables, setTables] = useState<Table[]>([]);
    const [reservations, setReservations] = useState<Reservation[]>([]);
    let reservationsPerTable = new Map<Reservation, Table[]>();


    useEffect(() => {
        async function loadTables() {
            try {
                const data = await fetchTables();
                setTables(data);
            } catch (error) {
                console.error('Error occurred loading tables:', error);
            }
        }
        loadTables().then();

        async function loadReservations(){
            try {
                const data = await fetchReservations();
                setReservations(data);
            } catch (error){
                console.error('Error occurred loading reservations:', error);
            }
        }
        loadReservations().then(() => {
            reservations.map((value: Reservation) => {
                reservationsPerTable.set(value, value.jointTables);
            });
        });
    }, [reservations, reservationsPerTable]);

    return (
        <>
            <div className="container" style={{marginTop: "10rem"}}>
                <div className="row gap-5">
                    {tables.map(table => (
                        <TableCard
                            table={table}
                            {/*TODO : Stick with the backend implementation of map*/}/>
                    ))}
                </div>
            </div>
        </>
    );
}

export default LoungeCardHolder;