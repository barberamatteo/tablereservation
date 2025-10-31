package it.matteobarbera.tablereservation.controller.reservation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.matteobarbera.tablereservation.cache.CacheConstants;
import it.matteobarbera.tablereservation.http.ReservationAPIResult;
import it.matteobarbera.tablereservation.http.response.CommonJSONBodies;
import it.matteobarbera.tablereservation.model.dto.ReservationDTO;
import it.matteobarbera.tablereservation.model.preferences.UserPreferences;
import it.matteobarbera.tablereservation.model.reservation.Reservation;
import it.matteobarbera.tablereservation.facade.ReservationHandlingFacade;
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
import static it.matteobarbera.tablereservation.logging.ReservationLog.*;


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



    @Operation(
            summary = "Add a reservation",
            description = "Adds a reservation specifying customer id, number of people, arrival and leave datetime"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Couldn't post the reservation due to errors in the request." +
                                    " Check the request parameters values."
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "No available tables for this reservation"
            ),
            @ApiResponse(responseCode = "200")
    })
    @CrossOrigin
    @PostMapping("/newreservation/")
    public ResponseEntity<?> newReservation(
            @RequestBody ReservationDTO reservationDTO
    ) {

        if (reservationDTO.getEndDateTime() == null) {
            try {
                reservationDTO.setEndDateTime(
                        DateUtils.offsetFrom(
                                userPreferences.DEFAULT_LEAVE_TIME_MINUTES_OFFSET,
                                reservationDTO.getStartDateTime()
                        )
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

    @Operation(
            summary = "Delete a reservation",
            description = "Deletes a reservation by specifying its ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "No reservation with ID <reservationId> was found"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error occurred while deleting reservation with ID <reservationId>" +
                            ": reservation is either invalid in its schedule or already deleted."
            ),
            @ApiResponse(
                    responseCode = "200",
                    description = "Reservation deleted successfully"
            )
    })
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

    @Operation(
            summary = "Get a reservation object",
            description = "Returns a reservation object by specifying its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "No reservation with ID <reservationId> was found"
            ),
            @ApiResponse(responseCode = "200")
    })
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

    @Operation(
            summary = "Get all reservation by day",
            description = "Returns all Reservation objects having a schedule with an arrivalDateTime in input day"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "409",
                    description = "There are no reservations yet today."
            ),
            @ApiResponse(responseCode = "200")
    })
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


    @Operation(
            summary = "Get all today's reservation",
            description = "Returns all Reservation objects having a schedule with an arrivalDateTime indicating today" +
                    "(the day this API call is submitted)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "409",
                    description = "There are no reservations yet today."
            ),
            @ApiResponse(responseCode = "200")
    })
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

    @Operation(
            summary = "Edit the number of people of a reservation",
            description = "Tries to modify the number of people of an existing reservation. If the table previously " +
                    "assigned to that reservation is capable enough, it will just modify the number of people without " +
                    "altering the schedule. In case the table isn't big enough, a table change (reschedule) is scheduled: " +
                    " an ActionCacheEntry is saved into server's memory and a random token is provided, so that if the " +
                    "admin intends to proceed (by calling the /confirm endpoint), the table change would take place." +
                    " If there's no other table, an error is raised."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Reservation with ID <reservationId> has been updated successfully with a new " +
                            "number of people of <newNumberOfPeople>"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "No reservation with ID <reservationId> was found"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "No reservation with ID <reservationId> was found in schedule. Maybe it was deleted?"
            ),
            @ApiResponse(
                    responseCode = "303",
                    description = "Updating reservation with id <reservationId> triggered the creation of the update" +
                            " action token."
            )
    })
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

    @Operation(
            summary = "Confirm the reschedule of a reservation",
            description = "Triggers the action bound to the token returned by the /editnumberofpeople endpoint " +
                    "(reschedule of a reservation)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(
                    responseCode = "409",
                    description = "The token provided has no action bound to it. It's either invalid or expired."
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "No available table for the change bound to the token provided." +
                            " The former reservation was rolled back as it was before the change request."
            )
    })
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


    /**
     * Default ResponseEntity, returned when an unexpected or unhandled error occurs
     * @return an error 500 response
     */
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
