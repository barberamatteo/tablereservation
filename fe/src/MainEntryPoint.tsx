import {Route, Routes} from "react-router";
import Lounge from "./Lounge.tsx";
import LoginPage from "./LoginPage.tsx";
import {useEffect} from "react";
import {useNavigate} from "react-router-dom";
import Messages from "./Messages.ts";

async function authCheck(){
    const response = await fetch(`${Messages.BACKEND}/auth/check`,
        {
            method: 'GET',
            credentials: "include",
        })
    return response.status === 204;
}

function MainEntryPoint() {
    const navigate = useNavigate();
    useEffect(() => {
        authCheck().then(r => {
            if (!r) navigate("/login")
        })
    }, []);
    return (
        <Routes>
            <Route path="/" element={<Lounge />} />
            <Route path="/login" element={<LoginPage />} />
        </Routes>
    );
}

export default MainEntryPoint;