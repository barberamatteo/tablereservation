package it.matteobarbera.tablereservation.model.table.user;

import it.matteobarbera.tablereservation.Constants;
import it.matteobarbera.tablereservation.model.customer.CustomerService;
import it.matteobarbera.tablereservation.model.preferences.UserPreferences;
import it.matteobarbera.tablereservation.model.reservation.Reservation;
import it.matteobarbera.tablereservation.model.reservation.ReservationsService;
import it.matteobarbera.tablereservation.model.reservation.ScheduleService;
import it.matteobarbera.tablereservation.model.reservation.strategies.ReservationStrategy;
import it.matteobarbera.tablereservation.model.table.CustomTable;
import it.matteobarbera.tablereservation.model.table.admin.TablesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;


@RestController
@RequestMapping(Constants.USER_RESERVATION_API_ENDPOINT)
public class UserReservationApiController {

    private static final Logger log = LoggerFactory.getLogger(UserReservationApiController.class);
    private final UserPreferences userPreferences;
    private final CustomerService customerService;

    //The Default strategy is FillLoungeFirst
    private final ReservationStrategy reservationStrategy;

    private final ReservationsService reservationsService;
    private final TablesService tablesService;
    private final ScheduleService scheduleService;

    @Autowired
    public UserReservationApiController(
            UserPreferences userPreferences,
            CustomerService customerService,
            ReservationStrategy reservationStrategy,
            ReservationsService reservationsService, TablesService tablesService, ScheduleService scheduleService) {
        this.userPreferences = userPreferences;
        this.customerService = customerService;
        this.reservationStrategy = reservationStrategy;
        this.reservationsService = reservationsService;
        this.tablesService = tablesService;
        this.scheduleService = scheduleService;
    }



    @CrossOrigin
    @PostMapping("/newreservation/")
    public Set<?> newReservation(
            @RequestParam(name = "customer") Long customerId,
            @RequestParam(name = "arrivalDateTime") String arrivalDateTime,
            @RequestParam(name = "leaveDateTime", required = false) String leaveDateTime,
            @RequestParam(name = "numberOfPeople") Integer numberOfPeople
    ){

        scheduleService.initScheduleIfAbsent(arrivalDateTime, leaveDateTime);

        LocalDateTime startDateTime = LocalDateTime.parse(arrivalDateTime);
        LocalDateTime endDateTime;
        if (leaveDateTime == null) {
            endDateTime = startDateTime.plusMinutes(userPreferences.DEFAULT_LEAVE_TIME_MINUTES_OFFSET);
        } else {
            endDateTime = LocalDateTime.parse(leaveDateTime);
        }

        
        Reservation reservation = new Reservation(
                startDateTime,
                endDateTime,
                customerService.getCustomerById(customerId),
                numberOfPeople
        );

        return reservationStrategy.postReservation(reservation);

    }

    @CrossOrigin
    @PostMapping("/newreservationpn/")
    public Set<?> newReservation(@RequestBody Map<String, Object> reservation){

        Long customerId = customerService.getCustomerByPhoneNumber(
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
    public Map<CustomTable, Set<Reservation>> getAllTodayReservation(){
        Set<Reservation> allReservations = reservationsService.getAllTodayReservations();
        Set<CustomTable> allTables = tablesService.getAllTables();
        log.atInfo().log("getAllReservation");
        return new HashMap<>(){{
           for (Reservation reservation : allReservations)
               for (CustomTable jointTable : reservation.getJointTables())
                   computeIfAbsent(jointTable, k -> new HashSet<>()).add(reservation);

           for (CustomTable table : allTables){
               if (!containsKey(table))
                   put(table, new HashSet<>());
           }
        }};
    }

}
