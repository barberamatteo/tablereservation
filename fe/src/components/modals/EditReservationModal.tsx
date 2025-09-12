import Reservation from "src/model/Reservation.ts";
import {Button, Form, Modal} from "react-bootstrap";
import {useRef, useState} from "react";
import RescheduleModal from "./RescheduleModal.tsx";
import Messages from "../../Messages.ts";


interface EditReservationModalProps {
    reservation: Reservation | null;
    handleClose: (exit_code: number) => void;
}

function EditReservationModal(props: EditReservationModalProps){
    const formRef = useRef<HTMLFormElement>(null);
    const [needToRescheduleModalShown, setNeedToRescheduleModalShown] = useState(false);
    const [token, setToken] = useState("");
    const handleDelete = async () => {
        await fetch(`${Messages.BACKEND}/api/v1.0.0/user/reservation/deletereservation/?reservation_id=${props.reservation?.id}`,
            {
                method: "DELETE",
                credentials: 'include'
            })
            .then(res => {
                if (res.ok) {
                    props.handleClose(4);
                }
            })
    }

    const handleEdit = async () =>{

        if (formRef.current) {
            const form = formRef.current;
            const formData = {
                numberOfPeople: Number.parseInt((form.elements.namedItem("numberOfPeople") as HTMLInputElement)?.value)
            }
            console.log("Doing call")
            const response = await callEditNumberOfPeopleEndpoint(formData.numberOfPeople)
            if (response.ok){
                props.handleClose(1);
            } else if(response.status === 303){
                const data = await response.json();
                setToken(data.token)
                setNeedToRescheduleModalShown(true);
            }
        }


    }

    async function callEditNumberOfPeopleEndpoint(numberOfPeople: number){
        return await fetch(`${Messages.BACKEND}/api/v1.0.0/user/reservation/editnumberofpeople/?reservation_id=${props.reservation?.id}&numberOfPeople=${numberOfPeople}`,
            {
                method: "PATCH",
                credentials: 'include'
            }
        )
    }
    
    return (
        <>
            <Modal show={props.reservation != null} onHide={() => props.handleClose(0)}>
                <Modal.Header closeButton>
                    <Modal.Title>Edit Reservation</Modal.Title>
                </Modal.Header>
                    <Modal.Body>
                        <Form ref={formRef}>
                            <Form.Group
                                className="mb-3"
                                controlId="customer">
                                    <Form.Label className="fw-bold">Customer</Form.Label>
                                    <br/>
                                    {props.reservation != null &&   <Form.Label>{props.reservation.customer.name+ " (" + props.reservation.customer.phoneNumber + ")"}</Form.Label>}
                            </Form.Group>
                            <Form.Group
                                className="mb-3"
                                controlId="datetimes">
                                    <Form.Label className="fw-bold">Date & Time</Form.Label>
                                    <br/>
                                    {props.reservation != null && <Form.Label>{props.reservation.interval.startDateTime + " to " + props.reservation.interval.endDateTime}</Form.Label>}
                            </Form.Group>
                            <Form.Group
                                className="mb-3"
                                controlId="numberOfPeople">
                                    <Form.Label className="fw-bold">Number of people</Form.Label>
                                    <br/>
                                    {props.reservation != null &&
                                        <Form.Control type="number" defaultValue={props.reservation.numberOfPeople} />}
                            </Form.Group>
                        </Form>
                </Modal.Body>
                <Modal.Footer>
                    {/*<Button variant="secondary" onClick={() => props.handleClose}>*/}
                    {/*    Close*/}
                    {/*</Button>*/}
                    <Button variant="danger" onClick={handleDelete}>Delete this reservation</Button>
                    <Button variant="primary" onClick={handleEdit}>
                        Edit
                    </Button>

                </Modal.Footer>
            </Modal>
            <RescheduleModal shown={needToRescheduleModalShown} token={token} handleClose={
                (exit_code: number) => {
                    setNeedToRescheduleModalShown(false)
                    props.handleClose(exit_code)
                }} />

        </>
    );
}

export default EditReservationModal;