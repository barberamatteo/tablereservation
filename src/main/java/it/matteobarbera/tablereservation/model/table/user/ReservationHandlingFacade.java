package it.matteobarbera.tablereservation.model.table.user;

import it.matteobarbera.tablereservation.cache.CacheConstants;
import it.matteobarbera.tablereservation.cache.CacheUtils;
import it.matteobarbera.tablereservation.cache.model.ActionCacheEntry;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.text.SimpleDateFormat;
import java.util.*;


@Component
public class ReservationHandlingFacade {

    private final CustomerService customerService;
    private final ScheduleService scheduleService;
    private final TablesService tablesService;
    private final ReservationsService reservationsService;
    private final CacheUtils cacheUtils;

    public ReservationHandlingFacade(
            CustomerService customerService,
            ScheduleService scheduleService,
            TablesService tablesService,
            ReservationsService reservationsService,
            CacheUtils cacheUtils) {
        this.customerService = customerService;
        this.scheduleService = scheduleService;
        this.tablesService = tablesService;
        this.reservationsService = reservationsService;
        this.cacheUtils = cacheUtils;
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


    private HashMap<CustomTable, Set<Reservation>> getAllReservationsMap(Set<Reservation> allReservations) {
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
        return deleteReservation(reservationById);
    }

    public ReservationAPIResult deleteReservation(Reservation reservation) {
        if (reservation == null)
            return new ReservationAPIResult.Failure(ReservationAPIError.NO_RESERVATION_WITH_ID);
        boolean isRemovedFromSchedule = scheduleService.removeReservationFromSchedule(reservation);
        reservationsService.deleteReservation(reservation);
        return (
                isRemovedFromSchedule
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

    @Transactional
    public ReservationAPIResult performActionFromToken(String token) {
        ActionCacheEntry<?> cacheQuery = cacheUtils.getActionCacheEntryBoundToToken(token);
        if (cacheQuery == null)
            return new ReservationAPIResult.Failure(ReservationAPIError.INVALID_TOKEN);

        if (Objects.equals(cacheQuery.action(), CacheConstants.CONFIRM_RESCHEDULE)){
            Reservation reservation = (Reservation) cacheQuery.obj();
            if (reservation == null) {
                return new ReservationAPIResult.Failure(ReservationAPIError.INVALID_TOKEN);
            }
            if (!deleteReservation(reservation).isSuccess()){
                return new ReservationAPIResult.Failure(ReservationAPIError.GENERAL_ERROR);
            }
            ReservationAPIResult recreateResult = newReservation(ReservationDTO.from(reservation));
            if (recreateResult.getStatus() == ReservationAPIError.NO_AVAILABLE_TABLES) {
                TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
                return new ReservationAPIResult.Failure(ReservationAPIError.NO_AVAILABLE_TABLES_FOR_CHANGES_REQUIRED);
            }
            if (recreateResult.isSuccess()) {
                return recreateResult;
            }
        }
        else {
            return new ReservationAPIResult.Failure(ReservationAPIError.GENERAL_ERROR);
        }
        return null;
    }

    public String triggerUpdateNumberOfPeopleTokenCreation(Long reservationId, Integer newNumberOfPeople) {
        Reservation reservation = reservationsService.getReservationById(reservationId);
        reservation.setNumberOfPeople(newNumberOfPeople);
        return createActionTokenBoundToReservation(CacheConstants.CONFIRM_RESCHEDULE, reservation);
    }

    private String createActionTokenBoundToReservation(String action, Reservation reservation) {
        return cacheUtils.createActionTokenBoundToObj(action, reservation);
    }


    public ReservationAPIResult getReservationById(Long id) {
        Reservation reservation = reservationsService.getReservationById(id);
        return (
                reservation == null
                ? new ReservationAPIResult.Failure(ReservationAPIError.NO_RESERVATION_WITH_ID)
                : new ReservationAPIResult.Success(reservation, ReservationAPIInfo.RESERVATION_FETCHED_OK)
        );
    }

    public ReservationAPIResult getAllReservationsByDay(String day) {
        Set<Reservation> reservationsByDay = reservationsService.getAllReservationsByDay(day);
        HashMap<CustomTable, Set<Reservation>> res = getAllReservationsMap(reservationsByDay);
        return (
                res.isEmpty()
                        ? new ReservationAPIResult.Failure(ReservationAPIError.NO_RESERVATION_YET_FOR_DAY)
                        : new ReservationAPIResult.Success(res, ReservationAPIInfo.RESERVATION_FETCHED_OK)
        );
    }

    public ReservationAPIResult getAllTodayReservations() {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        return getAllReservationsByDay(date);
    }
}
