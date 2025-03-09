package it.matteobarbera.tablereservation.model.table.user;

import it.matteobarbera.tablereservation.Constants;
import it.matteobarbera.tablereservation.http.response.CommonBodies;
import it.matteobarbera.tablereservation.model.dto.ReservationDTO;
import it.matteobarbera.tablereservation.model.reservation.Reservation;
import it.matteobarbera.tablereservation.model.table.AbstractTable;
import it.matteobarbera.tablereservation.model.table.CustomTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping(Constants.USER_RESERVATION_API_ENDPOINT)
public class UserReservationApiController {

    private final ReservationHandlingFacade reservationHandlingFacade;

    @Autowired
    public UserReservationApiController(ReservationHandlingFacade reservationHandlingFacade) {
        this.reservationHandlingFacade = reservationHandlingFacade;
    }


    @CrossOrigin
    @PostMapping("/newreservation/")
    public ResponseEntity<?> newReservation(
            @RequestParam(name = "customer") Long customerId,
            @RequestParam(name = "arrivalDateTime") String arrivalDateTime,
            @RequestParam(name = "leaveDateTime", required = false) String leaveDateTime,
            @RequestParam(name = "numberOfPeople") Integer numberOfPeople
    ) {

        ReservationDTO reservationDTO = new ReservationDTO(
                customerId,
                arrivalDateTime,
                leaveDateTime,
                numberOfPeople
        );

        Set<AbstractTable> response = reservationHandlingFacade.newReservation(reservationDTO);

        return (
                response.isEmpty()
                ? ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(CommonBodies.failure(
                                HttpStatus.CONFLICT.value(),
                                "No available tables for this reservation"
                        ))

                : ResponseEntity.ok(response)
        );

    }

    @CrossOrigin
    @PostMapping("/newreservationpn/")
    public ResponseEntity<?> newReservation(@RequestBody Map<String, Object> reservation){

        Long customerId = reservationHandlingFacade.getCustomerByPhoneNumber(
                (String) reservation.get("customerPhoneNumber")
        ).getId();
        return newReservation(
                customerId,
                (String) reservation.get("arrivalDateTime"),
                (String) reservation.get("leaveDateTime"),
                Integer.parseInt((String) reservation.get("numberOfPeople"))
        );

    }

    @CrossOrigin
    @GetMapping("/getall/")
    public ResponseEntity<?> getAllTodayReservations(){
        Map<CustomTable, Set<Reservation>> response = reservationHandlingFacade.getAllTodayReservations();
        return (
                response.isEmpty()
                ? ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(CommonBodies.failure(
                                HttpStatus.CONFLICT.value(),
                                "There are no reservations yet today."
                        ))
                : ResponseEntity.ok(response)
        );
    }

}
