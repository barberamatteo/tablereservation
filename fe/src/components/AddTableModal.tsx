import {Button, Form, Modal} from "react-bootstrap";
import CustomersDropdownSearch from "./CustomersDropdownSearch.tsx";
import DatePicker from "./datetime/DatePicker.tsx";
import TimePicker from "./datetime/TimePicker.tsx";
import {useRef} from "react";

interface AddTableModalProps{
    shown: boolean;
    handleClose: (exit_code: number) => void;
}
function AddTableModal(props: AddTableModalProps){
    const formRef = useRef<HTMLFormElement>(null);

    return (
        <>
            <Modal show={props.shown} onHide={() => props.handleClose(0)}>
                <Modal.Header closeButton>
                    <Modal.Title>Add Table</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form ref={formRef}>
                        <Form.Group
                            className="mb-3"
                            controlId="numberOfPeople">
                            <Form.Label>Table Category</Form.Label>
                            <Form.Select>

                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="reservationDate">
                            <Form.Label className="active">Select date of the reservation</Form.Label>
                            <DatePicker date={date} callback={(newDate: string) => setDate(newDate)} showDefault={false} />
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="reservationTime">
                            <Form.Label className="active">Select time of the reservation</Form.Label>
                            <TimePicker time={time} callback={(newTime: string) => setTime(newTime)} />
                        </Form.Group>
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => props.handleClose(0)}>
                        Close
                    </Button>
                    <Button variant="primary" onClick={addReservation}>
                        Save Changes
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}

export default AddTableModal;