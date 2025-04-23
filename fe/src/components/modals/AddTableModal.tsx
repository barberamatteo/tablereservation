import {Button, Form, Modal} from "react-bootstrap";
import {useEffect, useRef, useState} from "react";
import TableDefinition from "../../model/TableDefinition.ts";

interface AddTableModalProps{
    shown: boolean;
    handleClose: (exit_code: number) => void;
}


async function addTableEndpointCall(formData: {
    category: string;
    number: number;
}) {
    return await fetch(`http://localhost:8080/api/v1.0.0/admin/tables/create/?category=${formData.category}&number=${formData.number}`,
        {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Transfer-Encoding': 'chunked',
                'Keep-Alive': 'timeout=60',
                'Connection': 'keep-alive',
                'Accept': 'application/json'
            }
        }
    );
}

function AddTableModal(props: AddTableModalProps){

    const [tableDefinitions, setTableDefinitions] = useState<TableDefinition[]>([]);
    const formRef = useRef<HTMLFormElement>(null);
    useEffect(() => {
        getAllDefinitions().then();
    }, []);

    const getAllDefinitions = async () => {
        const response = await fetch('http://0.0.0.0:8080/api/v1.0.0/admin/tablesdefinition/getall/');
        if (response.ok){
            const data = await response.json();
            setTableDefinitions(
                data.map((td: {
                categoryName: string;
                standaloneCapacity: number
                }) => new TableDefinition(td.categoryName, td.standaloneCapacity))
            );
        }
        return [] as TableDefinition[];
    }

    const addTable = async ()=>{
        if (formRef.current) {
            const form = formRef.current;
            const formData = {
                category: (form.elements.namedItem("tableDefinition") as HTMLInputElement)?.value,
                number: parseInt((form.elements.namedItem("tableNumber") as HTMLInputElement)?.value)
            }
            await addTableEndpointCall(
                formData
            ).then((value: Response) => {
                if (value.ok){
                    props.handleClose(1);
                } else {
                    props.handleClose(2);
                }
            })
        }
    }


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
                            controlId="tableDefinition">
                            <Form.Label>Table Category</Form.Label>
                            <Form.Select>
                                <option>Select table definition ...</option>
                                {tableDefinitions.map((def: TableDefinition) => (
                                 <option value={def.categoryName}>{def.categoryName + " (" + def.standaloneCapacity + ")"}</option>
                                ))}
                            </Form.Select>
                        </Form.Group>
                        <Form.Group
                            className="mb-3"
                            controlId="tableNumber">
                            <Form.Label>Insert the table number</Form.Label>
                            <Form.Control type="number" min="1"/>
                        </Form.Group>
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => props.handleClose(0)}>
                        Close
                    </Button>
                    <Button variant="primary" onClick={addTable}>
                        Add Table
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}

export default AddTableModal;