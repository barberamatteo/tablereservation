package it.matteobarbera.tablereservation.model.table.user;

import it.matteobarbera.tablereservation.cache.CacheConstants;
import it.matteobarbera.tablereservation.http.ReservationAPIResult;
import it.matteobarbera.tablereservation.http.request.ReservationAPIRequest;
import it.matteobarbera.tablereservation.http.response.CommonJSONBodies;
import it.matteobarbera.tablereservation.model.dto.ReservationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static it.matteobarbera.tablereservation.Constants.USER_RESERVATION_API_ENDPOINT;
import static it.matteobarbera.tablereservation.http.ReservationAPIError.*;
import static it.matteobarbera.tablereservation.http.ReservationAPIInfo.*;


@RestController
@RequestMapping(USER_RESERVATION_API_ENDPOINT)
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

        ReservationAPIResult result = reservationHandlingFacade.newReservation(reservationDTO);

        if (result.isSuccess()) {
            if (result.getStatus() == RESERVATION_CREATED_OK) {
                return ResponseEntity.ok(
                        result.getSuccess().getResult()
                );
            }
        } else {
            if (result.getStatus() == NO_AVAILABLE_TABLES) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(
                                CommonJSONBodies.fromStatusAndMsg(
                                        HttpStatus.CONFLICT.value(),
                                        NO_AVAILABLE_TABLES.getMessage()
                                )
                        );
            }
        }

        return defaultError();


    }


    @CrossOrigin
    @PostMapping("/newreservationpn/")
    // TODO: Must be tested.
    public ResponseEntity<?> newReservation(
            @RequestBody ReservationAPIRequest reservation
    ){

        try {
            return newReservation(
                    reservation.getCustomerId(),
                    reservation.getStartDateTime(),
                    reservation.getEndDateTime(),
                    reservation.getNumberOfPeople()
            );
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(
                            CommonJSONBodies.fromStatusAndMsg(
                                    HttpStatus.BAD_REQUEST.value(),
                                    INVALID_DATA_FORMAT.getMessage()
                            )
                    );
        }
    }

    @DeleteMapping("/deletereservation/")
    public ResponseEntity<?> deleteReservation(
            @RequestParam(name="reservation_id") Long reservationId
    ){

        ReservationAPIResult result = reservationHandlingFacade.deleteReservation(reservationId);

        if (result.isSuccess()) {
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON).body(
                            CommonJSONBodies.fromStatusAndMsg(
                                    HttpStatus.OK.value(),
                                    RESERVATION_DELETED_OK.getMessage()
                            )
                    );
        } else {
            if (result.getStatus() == NO_RESERVATION_WITH_ID) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(
                                CommonJSONBodies.fromStatusAndMsg(
                                        HttpStatus.BAD_REQUEST.value(),
                                        NO_RESERVATION_WITH_ID.getMessage(reservationId)
                                )
                        );
            }
            if (result.getStatus() == RESERVATION_DELETE_ERROR) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(
                                CommonJSONBodies.fromStatusAndMsg(
                                        HttpStatus.BAD_REQUEST.value(),
                                        RESERVATION_DELETE_ERROR.getMessage()
                                )
                        );
            }
        }

        return defaultError();
    }
    @CrossOrigin
    @GetMapping("/getall/")
    public ResponseEntity<?> getAllTodayReservations(){
        ReservationAPIResult result = reservationHandlingFacade.getAllTodayReservations();

        if (result.isSuccess()){
            return ResponseEntity.ok(result.getSuccess().getResult());
        } else {
            if (result.getStatus() == NO_RESERVATION_YET_TODAY){
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(
                                CommonJSONBodies.fromStatusAndMsg(
                                        HttpStatus.CONFLICT.value(),
                                        NO_RESERVATION_YET_TODAY.getMessage()
                                )
                        );
            }
        }
        return defaultError();
    }

    @PatchMapping("/editnumberofpeople/")
    public ResponseEntity<?> editReservationNumberOfPeople(
            @RequestParam(name = "reservation_id") Long reservationId,
            @RequestParam(name = "numberOfPeople") Integer newNumberOfPeople
    ){

        ReservationAPIResult result =
                reservationHandlingFacade.editReservationNumberOfPeople(reservationId, newNumberOfPeople);
        if (result.isSuccess() && result.getStatus() == RESERVATION_UPDATE_OK) {
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(
                            CommonJSONBodies.fromStatusAndMsg(
                                    HttpStatus.OK.value(),
                                    RESERVATION_UPDATE_OK.getMessage(reservationId, newNumberOfPeople)
                            )
                    );
        } else {
            if (result.getStatus() == NO_RESERVATION_WITH_ID) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(
                                CommonJSONBodies.fromStatusAndMsg(
                                        HttpStatus.BAD_REQUEST.value(),
                                        NO_RESERVATION_WITH_ID.getMessage(reservationId)
                                )
                        );
            }
            if (result.getStatus() == NO_RESERVATION_WITH_ID_IN_SCHEDULE){
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(
                                CommonJSONBodies.fromStatusAndMsg(
                                        HttpStatus.BAD_REQUEST.value(),
                                        NO_RESERVATION_WITH_ID_IN_SCHEDULE.getMessage(reservationId)
                                )
                        );
            }
            if (result.getStatus() == NEED_TO_RESCHEDULE){
                String token = reservationHandlingFacade.triggerUpdateNumberOfPeopleTokenCreation(
                        reservationId,
                        newNumberOfPeople
                );


                return ResponseEntity
                        .status(HttpStatus.SEE_OTHER.value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(
                                CommonJSONBodies.fromStatusAndMsgAndX(
                                        HttpStatus.SEE_OTHER.value(),
                                        NEED_TO_RESCHEDULE.getMessage(),
                                        Map.of(CacheConstants.TOKEN, token)
                                )
                        );
            }

        }



        return defaultError();
    }

    @PatchMapping("/confirm/")
    public ResponseEntity<?> performPatchByToken(
            @RequestParam(name = "token") String token
    ){
        ReservationAPIResult res = reservationHandlingFacade.performActionFromToken(token);
        if (res.isSuccess()){
            return ResponseEntity.ok(
                    res.getSuccess().getResult()
            );
        } else {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(
                            CommonJSONBodies.fromStatusAndMsg(
                                    HttpStatus.CONFLICT.value(),
                                    res.getFailure().getError().getMessage()
                            )
                    );
        }
    }

    private ResponseEntity<String> defaultError(){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        CommonJSONBodies.fromStatusAndMsg(
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                GENERAL_ERROR.getMessage()
                        )
                );
    }

}
