import {Button, Modal} from "react-bootstrap";
import Messages from "../../Messages.ts";
interface RescheduleModalProps {
    shown: boolean;
    handleClose: (exit_code: number) => void;
    token: string;
}

function RescheduleModal(props: RescheduleModalProps) {
    const callToConfirmEndpoint = async () =>{
        return await fetch(`${Messages.BACKEND}/api/v1.0.0/user/reservation/confirm/?token=${props.token}`,
            {
                method: "PATCH",
                credentials: "include"
            })
    }
     const handleReschedule = async () => {
        const response = await callToConfirmEndpoint()
         if (response.ok){
             console.log("Entroi qu")
             props.handleClose(1);
         } else {
             props.handleClose(2);
         }
     }

    return (
        <Modal show={props.shown} onHide={() => props.handleClose(0)}>
            <Modal.Header closeButton>
                <Modal.Title>Need to reschedule</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                The table assigned to this reservation is too small for the edit you required. Would you like to reschedule to another table (if available)?
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={() => props.handleClose(0)}>
                    No
                </Button>
                <Button variant="primary" onClick={handleReschedule}>
                    Yes
                </Button>
            </Modal.Footer>
        </Modal>
    )
}

export default RescheduleModal;
