import {Button, Form, Modal} from "react-bootstrap";
import {useRef} from "react";

interface AddCustomerModalProps {
    shown: boolean;
    handleClose: (exit_code: number) => void;
}

async function addCustomerCall(formData: {
    phoneNumber: string,
    name: string,
    email: string
}){
    return await fetch(`http://localhost:8080/api/v1.0.0/customer/newcustomer?phoneNumber=${formData.phoneNumber}&name=${formData.name}&email=${formData.email}`, {
        method: 'POST',
        credentials: 'include'
    });
}
function AddCustomerModal(props: AddCustomerModalProps){

    const formRef = useRef<HTMLFormElement>(null);

    const addCustomer = () => {
        if (formRef.current) {
            const form = formRef.current;
            const formData = {
                phoneNumber: (form.elements.namedItem("phoneNumber") as HTMLInputElement)?.value,
                name: (form.elements.namedItem("name") as HTMLInputElement)?.value,
                email: (form.elements.namedItem("email") as HTMLInputElement)?.value,
            };
            addCustomerCall(formData).then((response) => {
                if (response.ok){
                    props.handleClose(1);
                } else {
                    props.handleClose(-1);
                }
            })
        }

    }

    return (
        <Modal show={props.shown} onHide={() => props.handleClose(0)}>
            <Modal.Header closeButton>
                <Modal.Title>Add Customer</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form ref={formRef}>
                    <Form.Group
                        className="mb-3"
                        controlId="phoneNumber">
                        <Form.Label>Phone number</Form.Label>
                        <Form.Control type="number" />
                    </Form.Group>
                    <Form.Group
                        className="mb-3"
                        controlId="name">
                        <Form.Label>Name</Form.Label>
                        <Form.Control />
                    </Form.Group>
                    <Form.Group
                        className="mb-3"
                        controlId="email">
                        <Form.Label>E-mail address</Form.Label>
                        <Form.Control />
                    </Form.Group>
                </Form>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={() => props.handleClose(0)}>
                    Close
                </Button>
                <Button variant="primary" onClick={addCustomer}>
                    Save Changes
                </Button>
            </Modal.Footer>
        </Modal>
    );
}

export default AddCustomerModal;