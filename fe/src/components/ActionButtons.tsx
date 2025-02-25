import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import AddReservationModal from "./AddReservationModal.tsx";
import {useState} from "react";
import InfoToast from "./InfoToast.tsx";
function ActionButtons() {

    const [addReservationModalOpen, setAddReservationModalOpen] = useState(false);
    const [addReservationToastExitCode, setAddReservationToastExitCode] = useState(0);
    const [addReservationToastShow, setAddReservationToastShow] = useState(false);
    return (
        <>
            <InfoToast
                shown={addReservationToastShow}
                setShow={setAddReservationToastShow}
                exit_code={addReservationToastExitCode}  />
            <div className="btn-group action-btns mx-auto" role="group" aria-label="Action Buttons">
                <button type="button" className="btn btn-outline-secondary"
                        onClick={() => setAddReservationModalOpen(true)}>
                    <img className="btn-icon" src="src/assets/btn_icon/add_rsv.png" alt="Add reservation"/>
                </button>

                    <AddReservationModal
                        shown={addReservationModalOpen}
                        handleClose={(exit_code) => {
                            setAddReservationModalOpen(false)
                            if (exit_code){
                                setAddReservationToastExitCode(exit_code)
                                setAddReservationToastShow(true)
                            }}
                        }/>


                <button type="button" className="btn btn-outline-secondary">
                    <img className="btn-icon" src="src/assets/btn_icon/add_tb.png" alt="Add table"/>
                </button>
                <button type="button" className="btn btn-outline-secondary">
                    <img className="btn-icon" src="src/assets/btn_icon/add_def.png" alt="Add table definition"/>
                </button>
            </div>


        </>
    )
}

export default ActionButtons;