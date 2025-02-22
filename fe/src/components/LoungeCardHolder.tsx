import { useState, useEffect } from 'react';
import Table from "./Table.ts"
import TableCard from "./TableCard.tsx";
async function fetchTables(): Promise<Table[]> {
    const response = await fetch('http://localhost:8080/api/v1.0.0/admin/tables/getall/');
    return await response.json();
}

function LoungeCardHolder() {
    const [tables, setTables] = useState<Table[]>([]);

    useEffect(() => {
        async function loadTables() {
            try {
                const data = await fetchTables();
                setTables(data);
            } catch (error) {
                console.error('Errore nel caricamento dei tavoli:', error);
            }
        }
        loadTables().then();
    }, []);

    return (
        <>
            <div className="container" style={{marginTop: "10rem"}}>
                <div className="row gap-5">
                    {tables.map(table => (
                        <TableCard table={table} />
                    ))}
                </div>
            </div>
        </>
    );
}

export default LoungeCardHolder;