import TimePicker from "./datetime/TimePicker.tsx";
import {useRef, useState} from "react";
import DatePicker from "./datetime/DatePicker.tsx";
import {Button, Form, Modal} from "react-bootstrap";
import 'bootstrap/dist/css/bootstrap.css'
import CustomersDropdownSearch from "./CustomersDropdownSearch.tsx";
import Customer from "../model/Customer.ts";



async function endpointCall(formData: {
    customerId: number;
    numberOfPeople: number;
    startDateTime: string;
}){

    console.log(JSON.stringify(formData));
    return await fetch('http://localhost:8080/api/v1.0.0/user/reservation/newreservationpn/', {
        method: 'POST',
        body: JSON.stringify(formData),
        headers: {
            'Content-Type': 'application/json',
            'Transfer-Encoding': 'chunked',
            'Keep-Alive': 'timeout=60',
            'Connection': 'keep-alive',
            'Accept': 'application/json'
        }
    });
}




function AddReservationModal({shown, handleClose}: {shown: boolean, handleClose: (exit_code: number) => void}) {
    const [selectedCustomer, setSelectedCustomer] = useState<Customer | null>(null);
    const [date, setDate] = useState<string>("");
    const [time, setTime] = useState<string>("");

    const formRef = useRef<HTMLFormElement>(null);

    const addReservation = async () => {
        if (selectedCustomer != null) {
            if (formRef.current) {
                const form = formRef.current;
                const formData = {
                    customerId: selectedCustomer.id,
                    numberOfPeople: Number.parseInt((form.elements.namedItem("numberOfPeople") as HTMLInputElement)?.value),
                    startDateTime: date + "T" + time
                };

                await endpointCall(
                    formData
                ).then((value) => {
                    //const body = value.json() as unknown as Table[];
                    if (value.ok && JSON.stringify(value.json()) != JSON.stringify('[]')) {
                        handleClose(1);
                    } else {
                        handleClose(2);
                    }
                    //setReservedTable(body);
                });
            }
        }
    }

    return (

        <>
            <Modal show={shown} onHide={() => handleClose(0)}>
                <Modal.Header closeButton>
                    <Modal.Title>Add Reservation</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form ref={formRef}>
                        <Form.Group className="mb-3" controlId="phoneNumber">
                            <Form.Label>Customer phone number</Form.Label>
                            <CustomersDropdownSearch onCustomerSelected={setSelectedCustomer}/>

                        </Form.Group>
                        <Form.Group
                            className="mb-3"
                            controlId="numberOfPeople">
                            <Form.Label>Insert the number of people</Form.Label>
                            <Form.Control type="number" max="50" min="1"/>
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="reservationDate">
                            <Form.Label className="active">Select date of the reservation</Form.Label>
                            <DatePicker date={date} callback={(newDate: string) => setDate(newDate)} />
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="reservationTime">
                            <Form.Label className="active">Select time of the reservation</Form.Label>
                            <TimePicker time={time} callback={(newTime: string) => setTime(newTime)} />
                        </Form.Group>
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => handleClose(0)}>
                        Close
                    </Button>
                    <Button variant="primary" onClick={addReservation}>
                        Save Changes
                    </Button>
                </Modal.Footer>
            </Modal>

        </>
    )
}


export default AddReservationModal;