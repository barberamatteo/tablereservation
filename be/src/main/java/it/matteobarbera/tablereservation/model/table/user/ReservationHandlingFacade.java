package it.matteobarbera.tablereservation.model.table.user;

import it.matteobarbera.tablereservation.http.ReservationAPIError;
import it.matteobarbera.tablereservation.http.ReservationAPIInfo;
import it.matteobarbera.tablereservation.http.ReservationAPIResult;
import it.matteobarbera.tablereservation.model.customer.Customer;
import it.matteobarbera.tablereservation.model.customer.CustomerService;
import it.matteobarbera.tablereservation.model.dto.ReservationDTO;
import it.matteobarbera.tablereservation.model.reservation.Reservation;
import it.matteobarbera.tablereservation.model.reservation.ReservationsService;
import it.matteobarbera.tablereservation.model.reservation.ScheduleService;
import it.matteobarbera.tablereservation.model.table.AbstractTable;
import it.matteobarbera.tablereservation.model.table.CustomTable;
import it.matteobarbera.tablereservation.model.table.admin.TablesService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Component
public class ReservationHandlingFacade {

    private final CustomerService customerService;
    private final ScheduleService scheduleService;
    private final TablesService tablesService;
    private final ReservationsService reservationsService;

    public ReservationHandlingFacade(
            CustomerService customerService,
            ScheduleService scheduleService,
            TablesService tablesService,
            ReservationsService reservationsService
    ) {
        this.customerService = customerService;
        this.scheduleService = scheduleService;
        this.tablesService = tablesService;
        this.reservationsService = reservationsService;
    }

    public ReservationAPIResult newReservation(ReservationDTO reservationDTO) {
        scheduleService.initScheduleIfAbsent(
                tablesService,
                reservationDTO.getStartDateTime(),
                reservationDTO.getEndDateTime()
        );

        Set<AbstractTable> reservationOutcome =  reservationsService.newReservation(
                customerService,
                scheduleService,
                reservationDTO
        );
        if (!reservationOutcome.isEmpty()) {
            return new ReservationAPIResult.Success(
                    reservationOutcome,
                    ReservationAPIInfo.RESERVATION_CREATED_OK
            );
        } else {
            return new ReservationAPIResult.Failure(
                    ReservationAPIError.NO_AVAILABLE_TABLES
            );
        }
    }

    public ReservationAPIResult getAllTodayReservations() {
        Set<Reservation> allReservations = reservationsService.getAllTodayReservations();
        HashMap<CustomTable, Set<Reservation>> res = getAllReservationMap(allReservations);
        return (
                res.isEmpty()
                ? new ReservationAPIResult.Failure(ReservationAPIError.NO_RESERVATION_YET_TODAY)
                : new ReservationAPIResult.Success(res, ReservationAPIInfo.RESERVATION_FETCHED_OK)
        );
    }

    private HashMap<CustomTable, Set<Reservation>> getAllReservationMap(Set<Reservation> allReservations) {
        Set<CustomTable> allTables = tablesService.getAllTables();
        return new HashMap<>(){{
            for (Reservation reservation : allReservations)
                for (CustomTable jointTable : reservation.getJointTables())
                    computeIfAbsent(jointTable, unused -> new HashSet<>()).add(reservation);

            for (CustomTable table : allTables){
                if (!containsKey(table))
                    put(table, new HashSet<>());
            }
        }};
    }


    public Customer getCustomerByPhoneNumber(String phoneNumber){
        return customerService.getCustomerByPhoneNumber(phoneNumber);
    }

    public ReservationAPIResult deleteReservation(Long reservationId) {
        Reservation reservationById = reservationsService.getReservationById(reservationId);
        if (reservationById == null)
            return new ReservationAPIResult.Failure(ReservationAPIError.NO_RESERVATION_WITH_ID);
        return (
                scheduleService.removeReservationFromSchedule(reservationById)
                ? new ReservationAPIResult.Success(ReservationAPIInfo.RESERVATION_DELETED_OK)
                : new ReservationAPIResult.Failure(ReservationAPIError.RESERVATION_DELETE_ERROR)
        );
    }

    public ReservationAPIResult editReservationNumberOfPeople(Long reservationId, Integer newNumberOfPeople) {
        Reservation reservationById = reservationsService.getReservationById(reservationId);
        if (reservationById == null)
            return new ReservationAPIResult.Failure(ReservationAPIError.NO_RESERVATION_WITH_ID);

        if (reservationsService.isNumberOfPeopleUpdatableWithoutRescheduling(reservationById, newNumberOfPeople)){
            return (
                    scheduleService.editReservationNumberOfPeopleInSchedule(reservationById, newNumberOfPeople)
                    ? new ReservationAPIResult.Success(ReservationAPIInfo.RESERVATION_UPDATE_OK)
                    : new ReservationAPIResult.Failure(ReservationAPIError.NO_RESERVATION_WITH_ID_IN_SCHEDULE)
            );
        } else {
            return new ReservationAPIResult.Failure(ReservationAPIError.NEED_TO_RESCHEDULE);
        }
    }
}
