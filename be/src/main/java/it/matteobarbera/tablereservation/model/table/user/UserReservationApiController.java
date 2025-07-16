package it.matteobarbera.tablereservation.model.table.user;

import it.matteobarbera.tablereservation.cache.CacheConstants;
import it.matteobarbera.tablereservation.http.ReservationAPIResult;
import it.matteobarbera.tablereservation.http.request.ReservationAPIRequest;
import it.matteobarbera.tablereservation.http.response.CommonJSONBodies;
import it.matteobarbera.tablereservation.model.dto.ReservationDTO;
import it.matteobarbera.tablereservation.model.preferences.UserPreferences;
import it.matteobarbera.tablereservation.model.reservation.Reservation;
import it.matteobarbera.tablereservation.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeParseException;
import java.util.Map;

import static it.matteobarbera.tablereservation.Constants.USER_RESERVATION_API_ENDPOINT;
import static it.matteobarbera.tablereservation.http.ReservationAPIError.*;
import static it.matteobarbera.tablereservation.http.ReservationAPIInfo.*;
import static it.matteobarbera.tablereservation.log.ReservationLog.*;


@RestController
@RequestMapping(USER_RESERVATION_API_ENDPOINT)
public class UserReservationApiController {

    private static final Logger log = LoggerFactory.getLogger(UserReservationApiController.class);
    private final ReservationHandlingFacade reservationHandlingFacade;
    private final UserPreferences userPreferences;

    @Autowired
    public UserReservationApiController(
            ReservationHandlingFacade reservationHandlingFacade,
            UserPreferences userPreferences
    ) {
        this.reservationHandlingFacade = reservationHandlingFacade;
        this.userPreferences = userPreferences;
    }



    @CrossOrigin
    @PostMapping("/newreservation/")
    public ResponseEntity<?> newReservation(
            @RequestParam(name = "customer") Long customerId,
            @RequestParam(name = "arrivalDateTime") String arrivalDateTime,
            @RequestParam(name = "leaveDateTime", required = false) String leaveDateTime,
            @RequestParam(name = "numberOfPeople") Integer numberOfPeople
    ) {

        if (leaveDateTime == null) {
            try {
                leaveDateTime = DateUtils.offsetFrom(
                        userPreferences.DEFAULT_LEAVE_TIME_MINUTES_OFFSET,
                        arrivalDateTime
                );
            } catch(DateTimeParseException e){
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

        ReservationDTO reservationDTO = new ReservationDTO(
                customerId,
                arrivalDateTime,
                leaveDateTime,
                numberOfPeople
        );

        ReservationAPIResult result = reservationHandlingFacade.newReservation(reservationDTO);


        if (result.isSuccess()) {
            if (result.getStatus() == RESERVATION_CREATED_OK) {
                log.atInfo().log(RESERVATION_CREATED, reservationDTO.toString());
                return ResponseEntity.ok(
                        result.getSuccess().getResult()
                );
            }
        } else {
            if (result.getStatus() == NO_AVAILABLE_TABLES) {
                log.atError().log(NO_AVAILABLE_TABLE_FOR_RESERVATION, reservationDTO.toString());
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

        return newReservation(
                reservation.getCustomerId(),
                reservation.getStartDateTime(),
                reservation.getEndDateTime(),
                reservation.getNumberOfPeople()
        );
        /*try {
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
        }*/
    }

    @DeleteMapping("/deletereservation/")
    public ResponseEntity<?> deleteReservation(
            @RequestParam(name="reservation_id") Long reservationId
    ){

        ReservationAPIResult result = reservationHandlingFacade.deleteReservation(reservationId);

        if (result.isSuccess()) {
            log.atInfo().log(RESERVATION_DELETED, reservationId);
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON).body(
                            CommonJSONBodies.fromStatusAndMsg(
                                    HttpStatus.OK.value(),
                                    RESERVATION_DELETED_OK.getMessage()
                            )
                    );
        } else {
            log.atError().log(NO_RESERVATION_TO_DELETE, reservationId);
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

    @GetMapping("/get/")
    public ResponseEntity<?> getReservation(@RequestParam Long id){
        ReservationAPIResult result = reservationHandlingFacade.getReservationById(id);
        if (result.isSuccess()) {
            log.atInfo().log(RESERVATION_FOUND_WITH_ID, ((Reservation) result.getSuccess().getResult()).getId());
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(
                            result.getSuccess().getResult()
                    );
        } else {
            log.atError().log(RESERVATION_WITH_ID_NOT_FOUND, id);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(
                            CommonJSONBodies.fromStatusAndMsg(
                                    HttpStatus.BAD_REQUEST.value(),
                                    result.getFailure().getError().getMessage(id)
                            )
                    );
        }
    }

    @CrossOrigin
    @GetMapping("/getallbyday/")
    public ResponseEntity<?> getAllReservationsByDay(@RequestParam String day){
        ReservationAPIResult result = reservationHandlingFacade.getAllReservationsByDay(day);
        if (result.isSuccess()){
            log.atInfo().log(ALL_RESERVATIONS_FOUND, "", day);
            return ResponseEntity.ok(result.getSuccess().getResult());
        } else {
            log.atError().log(NO_RESERVATIONS_FOUND);
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

    @CrossOrigin
    @GetMapping("/getall/")
    public ResponseEntity<?> getAllTodayReservations(){


        ReservationAPIResult result = reservationHandlingFacade.getAllTodayReservations();
        if (result.isSuccess()){
            log.atInfo().log(ALL_RESERVATIONS_FOUND, "to", "");
            return ResponseEntity.ok(result.getSuccess().getResult());
        } else {
            log.atError().log(NO_RESERVATIONS_FOUND);
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

    @CrossOrigin
    @PatchMapping("/editnumberofpeople/")
    public ResponseEntity<?> editReservationNumberOfPeople(
            @RequestParam(name = "reservation_id") Long reservationId,
            @RequestParam(name = "numberOfPeople") Integer newNumberOfPeople
    ){

        ReservationAPIResult result =
                reservationHandlingFacade.editReservationNumberOfPeople(reservationId, newNumberOfPeople);
        if (result.isSuccess() && result.getStatus() == RESERVATION_UPDATE_OK) {
            log.atInfo().log(RESERVATION_UPDATED, reservationId);
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
                log.atError().log(NO_RESERVATION_TO_UPDATE, reservationId);
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
                log.atError().log(NO_RESERVATION_TO_UPDATE_IN_SCHEDULE, reservationId);
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
                log.atWarn().log(RESERVATION_TO_RESCHEDULE, reservationId);

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

    @CrossOrigin
    @PatchMapping("/confirm/")
    public ResponseEntity<?> performPatchByToken(
            @RequestParam(name = "token") String token
    ){
        ReservationAPIResult res = reservationHandlingFacade.performActionFromToken(token);
        if (res.isSuccess()){
            log.atInfo().log(RESERVATION_UPDATED_AFTER_TOKEN_CONSUMING, token);
            return ResponseEntity.ok(
                    res.getSuccess().getResult()
            );
        } else {
            log.atError().log(RESERVATION_NOT_UPDATED_AFTER_TOKEN_CONSUMING, token);
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
        log.atError().log(GENERIC_ERROR);
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
