package it.matteobarbera.tablereservation.model.table.user;

import it.matteobarbera.tablereservation.Constants;
import it.matteobarbera.tablereservation.http.response.CommonJSONBodies;
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
                        .body(CommonJSONBodies.fromStatusAndMsg(
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

    @DeleteMapping("/deletereservation/")
    public ResponseEntity<?> deleteReservation(
            @RequestParam(name="reservation_id") Long reservationId
    ){
        return (
                reservationHandlingFacade.deleteReservation(reservationId)
                ? ResponseEntity.ok(
                        CommonJSONBodies.fromStatusAndMsg(
                                HttpStatus.OK.value(),
                                "Reservation deleted successfully"
                        )
                )
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        CommonJSONBodies.fromStatusAndMsg(
                                HttpStatus.BAD_REQUEST.value(),
                                "No resevation with ID " + reservationId + " was found"
                        )
                )
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
                        .body(CommonJSONBodies.fromStatusAndMsg(
                                HttpStatus.CONFLICT.value(),
                                "There are no reservations yet today."
                        ))
                : ResponseEntity.ok(response)
        );
    }

    @PatchMapping
    public ResponseEntity<?> editReservationNumberOfPeople(
            @RequestParam Long reservationId,
            @RequestParam Integer newNumberOfPeople
    ){
        reservationHandlingFacade.editReservationNumberOfPeople(reservationId, newNumberOfPeople);
    }

}
