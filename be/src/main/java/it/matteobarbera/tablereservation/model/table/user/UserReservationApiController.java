package it.matteobarbera.tablereservation.model.table.user;

import it.matteobarbera.tablereservation.Constants;
import it.matteobarbera.tablereservation.model.customer.CustomerService;
import it.matteobarbera.tablereservation.model.preferences.UserPreferences;
import it.matteobarbera.tablereservation.model.reservation.Interval;
import it.matteobarbera.tablereservation.model.reservation.Reservation;
import it.matteobarbera.tablereservation.model.reservation.strategies.FillLoungeFirst;
import org.hibernate.internal.build.AllowNonPortable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@RestController
@RequestMapping(Constants.USER_RESERVATION_API_ENDPOINT)
public class UserReservationApiController {

    private final UserPreferences userPreferences;
    private final CustomerService customerService;
    private final FillLoungeFirst fillLoungeFirst;

    @Autowired
    public UserReservationApiController(UserPreferences userPreferences, CustomerService customerService, FillLoungeFirst fillLoungeFirst) {
        this.userPreferences = userPreferences;
        this.customerService = customerService;
        this.fillLoungeFirst = fillLoungeFirst;
    }

    @PostMapping("/newreservation/")
    public String newReservation(
            @RequestParam(name = "customer") Long customerId,
            @RequestParam(name = "arrivalDateTime") String arrivalDateTime,
            @RequestParam(name = "leaveDateTime", required = false) String leaveDateTime,
            @RequestParam(name = "numberOfPeople") Integer numberOfPeople
    ){
        LocalDateTime startDateTime = LocalDateTime.parse(arrivalDateTime);
        LocalDateTime endDateTime;
        if (leaveDateTime == null) {
            endDateTime = startDateTime.plusMinutes(userPreferences.DEFAULT_LEAVE_TIME_MINUTES_OFFSET);
        } else {
            endDateTime = LocalDateTime.parse(leaveDateTime);
        }


        Reservation reservation = new Reservation(
                null,
                new Interval(startDateTime, endDateTime),
                customerService.getCustomerById(customerId),
                numberOfPeople
        );

        fillLoungeFirst.postReservation(reservation).runAll();

        return null;
    }
}
