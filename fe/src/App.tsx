import './App.css'
import MainEntryPoint from "./MainEntryPoint.tsx";
import {ToastProvider} from "./components/ToastPortal.tsx";

function App() {

  return (
    <>
        <ToastProvider>
            <MainEntryPoint />
        </ToastProvider>
    </>
  )
}

export default App
