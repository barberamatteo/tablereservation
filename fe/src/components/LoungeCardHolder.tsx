import { useState, useEffect } from 'react';
import Table from "../model/Table.ts"
import Reservation from "../model/Reservation.ts"
import TableCard from "./TableCard.tsx";
import reservation from "../model/Reservation.ts";
async function fetchTables(): Promise<Table[]> {
    const response = await fetch('http://localhost:8080/api/v1.0.0/admin/tables/getall/');
    return await response.json();
}

async function fetchReservations(): Promise<Map<string, Reservation[]>>{
    const response = await fetch('http://localhost:8080/api/v1.0.0/user/reservation/getall/');
    return await response.json();
}

function LoungeCardHolder() {
    const [tables, setTables] = useState<Table[]>([]);
    const [reservations, setReservations] = useState<Map<string, Reservation[]>>(new Map());


    useEffect(() => {
        async function loadTables() {
            try {
                const data = await fetchTables();
                setTables(data);
            } catch (error) {
                console.error('Error occurred loading tables:', error);
            }
        }
        loadTables().then(() => console.log(tables));

        async function loadReservations(){
            try {
                const data = await fetchReservations();
                setReservations(data);
            } catch (error){
                console.error('Error occurred loading reservations:', error);
            }
        }
        loadReservations().then();

    }, [tables]);

    return (
        <>
            <div className="container" style={{marginTop: "10rem"}}>
                <div className="row gap-5">
                    {/*

                        Array.from(reservations.entries()).map((row, index) => (
                        <TableCard table={JSON.parse(row[0]) as Table} reservations={row[1]}/>
                    ))
                    */}
                </div>
            </div>
        </>
    );
}

export default LoungeCardHolder;