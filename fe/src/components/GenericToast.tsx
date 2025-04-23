import {Toast, ToastContainer} from "react-bootstrap";
import * as React from "react";

interface GenericToastProps {
    shown: boolean;
    setShow: (visible: boolean) => void
    isSuccess: boolean;
    children: React.ReactNode;
}

function GenericToast(props: GenericToastProps) {

    const toastIconsPaths = {
        success : "src/assets/check.png",
        failure : "src/assets/failure.png"
    }

    const toastTitles = {
        success : "Success",
        failure : "Failure"
    }
    return (
        <>
            <ToastContainer position="bottom-end">
                <Toast
                    className="mb-3 me-3"
                    show={props.shown}
                    delay={10000}
                    autohide
                    onClose={() => props.setShow(false)}>
                    <Toast.Header closeButton={true}>
                        <img
                            src={props.isSuccess ? toastIconsPaths.success : toastIconsPaths.failure}
                            className="rounded me-2"
                            alt="Toast icon"
                        />
                        <strong className="me-auto">{props.isSuccess ? toastTitles.success : toastTitles.failure}</strong>
                        <small>now</small>
                    </Toast.Header>
                    <Toast.Body>
                        {props.children}
                    </Toast.Body>
                </Toast>
            </ToastContainer>
        </>
    );
}

export default GenericToast;