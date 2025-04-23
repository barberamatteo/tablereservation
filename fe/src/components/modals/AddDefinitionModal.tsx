import {Button, Form, Modal} from "react-bootstrap";
import {useRef} from "react";

interface AddDefinitionModalProps {
    shown: boolean;
    handleClose: (exit_code: number) => void;
}

async function addDefinitionEndpointCall(formData: { name: string; standaloneCapacity: number }){
    return await fetch(`http://localhost:8080/api/v1.0.0/admin/tablesdefinition/define/?category=${formData.name}&standaloneCapacity=${formData.standaloneCapacity}`,
        {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Transfer-Encoding': 'chunked',
                'Keep-Alive': 'timeout=60',
                'Connection' : 'keep-alive',
                'Accept': 'application/json'
            }
        })
}

function AddDefinitionModal(props: AddDefinitionModalProps) {

    const formRef = useRef<HTMLFormElement>(null);
    const addDefinition = async () => {
        if (formRef.current) {
            const form = formRef.current;
            const formData = {
                name: (form.elements.namedItem("definitionName") as HTMLInputElement)?.value,
                standaloneCapacity: parseInt((form.elements.namedItem("standaloneCapacity") as HTMLInputElement)?.value),
            }
            await addDefinitionEndpointCall(
                formData
            ).then((value) => {
                if (value.ok) {
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
                  <Modal.Title>Add Table definition</Modal.Title>
              </Modal.Header>
              <Modal.Body>
                  <Form ref={formRef}>
                      <Form.Group className="mb-3" controlId="definitionName">
                          <Form.Label>Table definition name</Form.Label>
                          <Form.Control type={"text"} />
                      </Form.Group>
                      <Form.Group
                          className="mb-3"
                          controlId="standaloneCapacity">
                          <Form.Label>Insert the capacity of the table</Form.Label>
                          <Form.Control type="number" min="1"/>
                      </Form.Group>
                  </Form>
              </Modal.Body>
              <Modal.Footer>
                  <Button variant="secondary" onClick={() => props.handleClose(0)}>
                      Close
                  </Button>
                  <Button variant="primary" onClick={addDefinition}>
                      Add definition
                  </Button>
              </Modal.Footer>
          </Modal>
      </>
    );
}

export default AddDefinitionModal;