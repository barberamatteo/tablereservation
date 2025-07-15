import Reservation from "src/model/Reservation.ts";
import {Button, Form, Modal} from "react-bootstrap";
import {useRef} from "react";


interface EditReservationModalProps {
    reservation: Reservation | null;
    handleClose: () => void;
}

function EditReservationModal(props: EditReservationModalProps){
    const formRef = useRef<HTMLFormElement>(null);

    const handleDelete = async () => {
        await fetch(`http://localhost:8080/api/v1.0.0/user/reservation/deletereservation/?reservation_id=${props.reservation?.id}`,
            {
                method: "DELETE",
                credentials: 'include'
            })
            .then(res => {
                if (res.ok) {
                    props.handleClose();
                }
            })
    }

    const handleEdit = async () =>{

        if (formRef.current) {
            const form = formRef.current;
            const formData = {
                numberOfPeople: Number.parseInt((form.elements.namedItem("numberOfPeople") as HTMLInputElement)?.value)
            }
            await callEditNumberOfPeopleEndpoint(formData.numberOfPeople)
        }


    }

    async function callEditNumberOfPeopleEndpoint(numberOfPeople: number){
        return await fetch(`http://localhost:8080/api/v1.0.0/user/reservation/editnumberofpeople/?reservation_id=${props.reservation?.id}&numberOfPeople=${formData.numberOfPeople}`)
    }
    
    return (
        <>
            <Modal show={props.reservation != null} onHide={props.handleClose}>
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
        </>
    );
}

export default EditReservationModal;