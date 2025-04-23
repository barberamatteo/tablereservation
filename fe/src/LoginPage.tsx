import Navbar from "./components/Navbar.tsx";
import {Button, Form} from "react-bootstrap";
import {useRef} from "react";
import {useNavigate} from "react-router-dom";

async function loginCall(username: string, password: string) {
    return await fetch("http://localhost:8080/login", {
        method: "POST",
        credentials: "include",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
        },
        body: new URLSearchParams({
            username: username,
            password: password
        }),
    })
}


function LoginPage(){

    const navigate = useNavigate();
    const formRef = useRef<HTMLFormElement>(null);

    const doLogin = async () => {
        if (formRef.current) {
            const form = formRef.current;
            const formData = {
                username: (form.elements.namedItem("username") as HTMLInputElement)?.value,
                password: (form.elements.namedItem("password") as HTMLInputElement)?.value,
            }
            await loginCall(formData.username, formData.password).then(value => {
                if (value.ok){
                    navigate("/");
                }
            })

        }
    }

    return (
        <>
            <Navbar />

            <Form ref={formRef} style={{width: '20%', height: '20%', top: '50%', left: '50%', transform: 'translate(200%, 150%)'  }}>
                <Form.Group className="mb-3" controlId="username">
                    <Form.Label>Username:</Form.Label>
                    <Form.Control type={"text"} />
                </Form.Group>

                <Form.Group className="mb-3" controlId="password">
                    <Form.Label>Password:</Form.Label>
                    <Form.Control type={"password"} />
                </Form.Group>

                <Button variant="primary" type="submit" onClick={doLogin}>
                    Login
                </Button>
            </Form>
        </>
    );
}

export default LoginPage;