import {Route, Routes} from "react-router";
import Lounge from "./Lounge.tsx";
import LoginPage from "./LoginPage.tsx";

function MainEntryPoint() {
    return (
        <Routes>
            <Route path="/" element={<Lounge />} />
            <Route path="/login" element={<LoginPage />} />
        </Routes>
    );
}

export default MainEntryPoint;