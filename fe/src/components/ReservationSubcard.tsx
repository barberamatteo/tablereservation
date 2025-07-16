import Reservation from "../model/Reservation.ts";
import {Button, Col, Container, Row} from "react-bootstrap";
import {BiPencil} from "react-icons/bi";
import {useState} from "react";
import EditReservationModal from "src/components/modals/EditReservationModal.tsx";
import GenericToast from "./GenericToast.tsx";

interface ReservationSubcardProps {
    reservations: Reservation[] | undefined;
}

function ReservationSubcard(props: ReservationSubcardProps) {
    const [reservationToEdit, setReservationToEdit] = useState<Reservation | null>(null);
    const [toastShown, setToastShown] = useState(false);
    const [isToastSuccess, setIsToastSuccess] = useState(false);
    const [toastMessage, setToastMessage] = useState("");
    if (props.reservations == null || props.reservations.length === 0) {
        return null;
    }
    return (

        <>
        {
            props.reservations.map((reservation: Reservation) => (
                <Container>
                    <Row>
                        <Col className="overflow-auto" style={{whiteSpace: "nowrap"}}>
                            <p>{reservation.customer.name + "(x" + reservation.numberOfPeople + ")"}</p>
                        </Col>
                        <Col>
                            <Button
                                variant="outline-danger"
                                size="sm"
                                onClick={() => setReservationToEdit(reservation)}>
                                <BiPencil />
                            </Button>
                        </Col>
                    </Row>
                    <md-divider />
                </Container>


            ))
        }

        <EditReservationModal reservation={reservationToEdit} handleClose={(exit_code: number) => {
            setReservationToEdit(null);
            switch (exit_code) {
                case 4:
                    setIsToastSuccess(true);
                    setToastMessage("Reservation deleted successfully.");
                    setToastShown(true);
                    break;
                case 0:
                    setToastShown(false);
                    break;
                case 1:
                    setIsToastSuccess(true);
                    setToastMessage("Reservation edited successfully.");
                    setToastShown(true);
                    break;
                case 2:
                    setIsToastSuccess(false);
                    setToastMessage("No available tables for the required operation.");
                    setToastShown(true);
                    break;
                default:
                    setIsToastSuccess(false);
                    setToastMessage("An unexpected error has occurred.");
                    setToastShown(true);
            }
        }} />

        <GenericToast shown={toastShown} setShow={setToastShown} isSuccess={isToastSuccess}>
            {toastMessage}
        </GenericToast>
        </>
)
}


export default ReservationSubcard;