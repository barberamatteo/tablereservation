import Navbar from "./components/Navbar.tsx";
import {Button, Form} from "react-bootstrap";
import {useEffect, useRef} from "react";
import {useNavigate} from "react-router-dom";
import Messages from "./Messages.ts";

async function loginCall(username: string, password: string) {
    return await fetch(`${Messages.BACKEND}/login`, {
        method: "POST",
        credentials: "include",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
        },
        body: "username=" + username + "&password=" + password,
    })
}

async function authCheck(){
    const response = await fetch(`${Messages.BACKEND}/auth/check`,
        {
            method: 'GET',
            credentials: "include",
        })
    return response.status === 204;
}
function LoginPage(){


    const navigate = useNavigate();
    const formRef = useRef<HTMLFormElement>(null);

    useEffect(() => {
        authCheck().then(authenticated => {
            console.log(`Authenticated = ${authenticated}`);
            if (authenticated) navigate("/")
        })
    });

    const doLogin = async () => {
        console.log("Doing login...");
        if (formRef.current) {
            const form = formRef.current;
            const formData = {
                username: (form.elements.namedItem("username") as HTMLInputElement)?.value,
                password: (form.elements.namedItem("password") as HTMLInputElement)?.value,
            }
            console.log("Entro")
            await loginCall(formData.username, formData.password).then(value => {
                console.log("Value = ", value.status);

                if (value.ok){
                    console.log("Successful");
                    navigate("/");
                } else {
                    console.log("Error");
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

                <Button variant="primary" onClick={doLogin}>
                    Login
                </Button>
            </Form>
        </>
    );
}

export default LoginPage;