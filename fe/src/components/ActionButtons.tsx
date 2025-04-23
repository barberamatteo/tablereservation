import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import AddReservationModal from "./modals/AddReservationModal.tsx";
import {useState} from "react";
import InfoToast from "./InfoToast.tsx";
import AddTableModal from "./modals/AddTableModal.tsx";
import GenericToast from "./GenericToast.tsx";
import Messages from "../Messages.ts";
import {Button, ButtonGroup, Image} from "react-bootstrap";
import AddDefinitionModal from "./modals/AddDefinitionModal.tsx";
interface ActionButtonsProps {
    onActionPerformed: () => void;
}
function ActionButtons(props: ActionButtonsProps) {

    const [addReservationModalOpen, setAddReservationModalOpen] = useState(false);
    const [addReservationToastSuccess, setAddReservationToastSuccess] = useState(false);
    const [addReservationToastShow, setAddReservationToastShow] = useState(false);

    const [addTableModalOpen, setAddTableModalOpen] = useState(false);
    const [addTableToastSuccess, setAddTableToastSuccess] = useState(false);
    const [addTableToastShow, setAddTableToastShow] = useState(false);

    const [addDefinitionModalOpen, setAddDefinitionModalOpen] = useState(false);
    const [addDefinitionToastSuccess, setAddDefinitionToastSuccess] = useState(false);
    const [addDefinitionToastShow, setAddDefinitionToastShow] = useState(false);
    return (
        <>
            <GenericToast
                shown={addReservationToastShow}
                setShow={setAddReservationToastShow}
                isSuccess={addReservationToastSuccess}>
                {
                    addReservationToastSuccess
                    ? Messages.RESERVATION_ADD_OK
                    : Messages.RESERVATION_ADD_FAILED
                }
            </GenericToast>

            <GenericToast
                shown={addTableToastShow}
                setShow={setAddTableToastShow}
                isSuccess={addTableToastSuccess}>
                {
                    addTableToastSuccess
                    ? Messages.TABLE_ADD_OK
                    : Messages.TABLE_ADD_FAILED
                }
            </GenericToast>

            <GenericToast
                shown={addDefinitionToastShow}
                setShow={setAddDefinitionToastShow}
                isSuccess={addDefinitionToastSuccess}>
                {
                    addDefinitionToastSuccess
                    ? Messages.DEFINITION_ADD_OK
                    : Messages.DEFINITION_ADD_FAILED
                }
            </GenericToast>



            <ButtonGroup style={{ marginTop: "100px"}} aria-label={"Action Buttons"} >
                <Button variant={"outline-secondary"} onClick={() => setAddReservationModalOpen(true)}>
                    <Image className="btn-icon" src="src/assets/btn_icon/add_rsv.png" alt="Add reservation" />
                </Button>
                <AddReservationModal
                    shown={addReservationModalOpen}
                    handleClose={(exit_code) => {
                        setAddReservationModalOpen(false)
                        if (exit_code) {
                            setAddReservationToastSuccess(false)
                            setAddReservationToastShow(true)
                            if (exit_code === 1) {
                                setAddReservationToastSuccess(true)
                                console.log("Settato true al toast")
                                props.onActionPerformed();
                            }
                        }
                    }}/>

                <Button variant={"outline-secondary"} onClick={() => setAddTableModalOpen(true)}>
                    <Image className="btn-icon" src="src/assets/btn_icon/add_tb.png" alt="Add table" />
                </Button>

                <AddTableModal shown={addTableModalOpen} handleClose={(exit_code) => {
                    setAddTableModalOpen(false);
                    if (exit_code) {
                        setAddTableToastSuccess(false);
                        setAddTableToastShow(true);
                        if (exit_code === 1) {
                            setAddTableToastSuccess(true);
                            props.onActionPerformed();
                        }
                    }
                }}/>

                <Button variant={"outline-secondary"} onClick={() => setAddDefinitionModalOpen(true)}>
                    <Image className="btn-icon" src="src/assets/btn_icon/add_def.png" alt="Add table definition" />
                </Button>

                <AddDefinitionModal shown={addDefinitionModalOpen} handleClose={(exit_code) => {
                    setAddDefinitionModalOpen(false);
                    if (exit_code) {
                        setAddDefinitionToastSuccess(false);
                        setAddDefinitionToastShow(true);
                        if (exit_code === 1) {
                            setAddDefinitionToastSuccess(true);
                            props.onActionPerformed();
                        }
                    }
                }}/>

            </ButtonGroup>


        </>
    );
}

export default ActionButtons;