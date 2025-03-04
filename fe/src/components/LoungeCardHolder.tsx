import {useEffect, useState} from 'react';
import Table from "../model/Table.ts"
import Reservation from "../model/Reservation.ts"
import TableCard from "./TableCard.tsx";

async function fetchTables(): Promise<Table[]> {
    const response = await fetch('http://localhost:8080/api/v1.0.0/admin/tables/getall/');
    return await response.json();
}

async function fetchReservations(): Promise<Map<string, Reservation[]>>{
    const response = await fetch('http://localhost:8080/api/v1.0.0/user/reservation/getall/');
    const data = await response.json();
    return new Map(Object.entries(data));
}

function rawToEffectiveMap(map: Map<string, Reservation[]>): Map<Table, Reservation[]> {
    const toRet = new Map<Table, Reservation[]>();
    map.forEach((v, k) => {
        toRet.set(JSON.parse(k) as Table, v);
    });
    return toRet;
}
function LoungeCardHolder() {
    const [tables, setTables] = useState<Table[]>([]);
    const [reservationsRaw, setReservationsRaw] = useState<Map<string, Reservation[]>>(new Map());
    const [reservations, setReservations] = useState<Map<Table, Reservation[]>>(new Map());
    useEffect(() => {
        async function loadData(){
            try {
                const tablesData = await fetchTables();
                setTables(tablesData);

                const reservationData = await fetchReservations();
                setReservationsRaw(reservationData);

                setReservations(rawToEffectiveMap(reservationData));

            } catch{
                console.log("Error during fetch of tables and reservationsRaw.");
            }
        }
        loadData().then();

    }, []);

    return (
        <>
            <div className="container" style={{marginTop: "10rem"}}>
                <div className="row gap-5">
                    {Array.from(reservations.entries()).map(([key, value]) => (
                        <TableCard table={key} reservations={value} />
                    ))}
                </div>
            </div>
        </>
    );
}

export default LoungeCardHolder;