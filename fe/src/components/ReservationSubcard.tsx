import Reservation from "../model/Reservation.ts";
import {Button, Col, Container, Row} from "react-bootstrap";
import {BiPencil} from "react-icons/bi";

interface ReservationSubcardProps {
    reservations: Reservation[] | undefined;
}

function ReservationSubcard(props: ReservationSubcardProps) {
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
                            <Button variant="outline-danger" size="sm">
                                <BiPencil />
                            </Button>
                        </Col>
                    </Row>
                    <md-divider />
                </Container>
            ))
        }
        </>
)
    ;
}


export default ReservationSubcard;