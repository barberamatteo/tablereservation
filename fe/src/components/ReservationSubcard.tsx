import Reservation from "../model/Reservation.ts";
import {Button, Col, Container, Row} from "react-bootstrap";
import {BiPencil} from "react-icons/bi";
import {useState} from "react";
import EditReservationModal from "src/components/modals/EditReservationModal.tsx";
import {useToast} from "./ToastPortal.tsx";

interface ReservationSubcardProps {
    reservations: Reservation[] | undefined;
}

function ReservationSubcard(props: ReservationSubcardProps) {
    const [reservationToEdit, setReservationToEdit] = useState<Reservation | null>(null);
    const {showToast} = useToast();
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
                    showToast("Reservation deleted successfully.", true);
                    break;
                case 0:
                    break;
                case 1:
                    showToast("Reservation edited successfully.", true);
                    break;
                case 2:
                    showToast("No available tables for the required operation.", false);
                    break;
                default:
                    showToast("An unexpected error has occurred.", false);
            }
        }} />

        </>
)
}


export default ReservationSubcard;