import './App.css'
import Navbar from "./components/Navbar.tsx";
import LoungeCardHolder from "./components/LoungeCardHolder.tsx";
import ActionButtons from "./components/ActionButtons.tsx";

function App() {

  return (
    <>
        <Navbar/>
        <ActionButtons/>
        <LoungeCardHolder/>
    </>
  )
}

export default App
