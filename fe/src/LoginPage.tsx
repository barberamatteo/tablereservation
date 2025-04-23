import Navbar from "./components/Navbar.tsx";
import {Button, Form} from "react-bootstrap";

function LoginPage(){
    return (
        <>
            <Navbar />

            <Form style={{width: '20%', height: '20%', top: '50%', left: '50%', transform: 'translate(200%, 150%)'  }}>
                <Form.Group className="mb-3" controlId="definitionName">
                    <Form.Label>Username:</Form.Label>
                    <Form.Control type={"text"} />
                </Form.Group>

                <Form.Group className="mb-3" controlId="definitionName">
                    <Form.Label>Password:</Form.Label>
                    <Form.Control type={"password"} />
                </Form.Group>

                <Button variant="primary" type="submit">
                    Login
                </Button>
            </Form>
        </>
    );
}

export default LoginPage;