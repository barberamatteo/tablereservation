import {useEffect, useState} from 'react';
import Table from "../model/Table.ts"
import Reservation from "../model/Reservation.ts"

import TableCard from "./TableCard.tsx";
import ActionButtons from "./ActionButtons.tsx";
import ScheduleSelector from "./ScheduleSelector.tsx";
import DateUtils from "./datetime/DateUtils.ts";

async function fetchTables(): Promise<Table[]> {
    const response = await fetch('http://localhost:8080/api/v1.0.0/admin/tables/getall/', {
        method: 'GET',
        credentials: 'include'
    });
    return await response.json();
}

async function fetchReservations(date: string): Promise<Map<string, Reservation[]>>{
    const response = await fetch('http://localhost:8080/api/v1.0.0/user/reservation/getallbyday/?day='+ date, {
        method: 'GET',
        credentials: 'include'
    });
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
    const [currentDate, setCurrentDate] = useState(getDay());
    const [tables, setTables] = useState<Table[]>([]);
    const [reservationsRaw, setReservationsRaw] = useState<Map<string, Reservation[]>>(new Map());
    const [reservations, setReservations] = useState<Map<Table, Reservation[]>>(new Map());

    async function loadData(date: string){
        console.log("Chiamata")
        persistDay(date);
        try {
            const tablesData = await fetchTables();
            setTables(tablesData);

            const reservationData = await fetchReservations(date);
            setReservationsRaw(reservationData);

            setReservations(rawToEffectiveMap(reservationData));

        } catch{
            console.log("Error during fetch of tables and reservationsRaw.");
        }
    }

    function getDay(): string{
        if (localStorage.getItem("day") == null)
            persistDay(DateUtils.today())

        console.log("Returning ", localStorage.getItem("day"));
        return localStorage.getItem("day") as string;
    }
    function persistDay(date: string){
        localStorage.setItem("day", date);
    }

    useEffect(() => {
        loadData(currentDate).then();
    }, [currentDate]);

    return (
        <>
            <div className="d-flex flex-row justify-content-between px-5 align-items-end">
                <div style={{width: '14rem'}}/>
                <div>
                    <ActionButtons onActionPerformed={() => loadData(DateUtils.today())}/>
                </div>
                <div>   
                    <ScheduleSelector init={DateUtils.toLocaleDate(getDay())} onChange={setCurrentDate}/>
                </div>
            </div>
            <div className="container" style={{marginTop: "10rem"}}>
                <div className="row align-items-start gap-5">
                    {Array.from(reservations.entries()).map(([key, value]) => (
                        <TableCard table={key} reservations={value} />
                    ))}
                </div>
            </div>
        </>
    );
}

export default LoungeCardHolder;