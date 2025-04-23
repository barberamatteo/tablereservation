import {Toast, ToastContainer} from "react-bootstrap"

interface InfoToastProps{
    shown: boolean;
    setShow: (visible: boolean) => void
    exit_code: number
}

const exit_code_rationales = [

    //
    {
        title: "",
        message: "",
        iconPath: ""
    },
    {
        title: "Success",
        message: "Reservation has been added successfully!",
        iconPath: "src/assets/check.png"
    },
    {
        title: "Failure",
        message: "Reservation was not added due to a failure!",
        iconPath: "src/assets/failure.png"
    },
    {
        title: "Success",
        message: "Table has been added successfully!",
        iconPath: "src/assets/check.png"
    },
    {
        title: "Failure",
        message: "Table was not added due to a failure!",
        iconPath: "src/assets/failure.png"
    }

]

function InfoToast(props: InfoToastProps){
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
                            src={exit_code_rationales[props.exit_code].iconPath}
                            className="rounded me-2"
                            alt="Check icon"
                        />
                        <strong className="me-auto">{exit_code_rationales[props.exit_code].title}</strong>
                        <small>now</small>
                    </Toast.Header>
                    <Toast.Body>
                        {exit_code_rationales[props.exit_code].message}
                    </Toast.Body>
                </Toast>
            </ToastContainer>
        </>
    )
}

export default InfoToast;