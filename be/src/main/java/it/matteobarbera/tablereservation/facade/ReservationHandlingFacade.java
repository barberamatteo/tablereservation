package it.matteobarbera.tablereservation.facade;

import it.matteobarbera.tablereservation.cache.CacheConstants;
import it.matteobarbera.tablereservation.cache.CacheUtils;
import it.matteobarbera.tablereservation.cache.ActionCacheEntry;
import it.matteobarbera.tablereservation.http.*;
import it.matteobarbera.tablereservation.mapper.ReservationMapper;
import it.matteobarbera.tablereservation.model.customer.Customer;
import it.matteobarbera.tablereservation.model.customer.NoSuchCustomerWithIdException;
import it.matteobarbera.tablereservation.model.table.layout.NoSuchLayoutWithIdException;
import it.matteobarbera.tablereservation.model.table.layout.SimpleMatrixLayout;
import it.matteobarbera.tablereservation.service.customer.CustomerService;
import it.matteobarbera.tablereservation.model.dto.ReservationDTO;
import it.matteobarbera.tablereservation.model.reservation.Reservation;
import it.matteobarbera.tablereservation.service.reservation.ReservationsService;
import it.matteobarbera.tablereservation.service.reservation.ScheduleService;
import it.matteobarbera.tablereservation.model.table.AbstractTable;
import it.matteobarbera.tablereservation.model.table.SimpleTable;
import it.matteobarbera.tablereservation.service.table.TablesService;
import it.matteobarbera.tablereservation.service.table.layout.TableLayoutService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


/**
 * Facade component that orchestrates all the domain services in the process of handling the HTTP calls to the
 * Reservations API endpoints, in order to achieve low coupling between services and controllers.
 */
@Component
public class ReservationHandlingFacade {

    private final CustomerService customerService;
    private final ScheduleService scheduleService;
    private final TablesService tablesService;
    private final ReservationsService reservationsService;
    private final CacheUtils cacheUtils;
    private final ModelMapper modelMapper;
    private final ReservationMapper reservationMapper;
    private final TableLayoutService tableLayoutService;

    public ReservationHandlingFacade(
            CustomerService customerService,
            ScheduleService scheduleService,
            TablesService tablesService,
            ReservationsService reservationsService,
            CacheUtils cacheUtils,
            ModelMapper modelMapper,
            ReservationMapper reservationMapper,
            TableLayoutService tableLayoutService) {
        this.customerService = customerService;
        this.scheduleService = scheduleService;
        this.tablesService = tablesService;
        this.reservationsService = reservationsService;
        this.cacheUtils = cacheUtils;
        this.modelMapper = modelMapper;
        this.reservationMapper = reservationMapper;
        this.tableLayoutService = tableLayoutService;
    }

    /**
     * Tries to create a new reservation
     * @param reservationDTO A DTO passed by the controller, containing the state of the reservation to be scheduled.
     * @param layoutId The chosen table layout id
     * @return a Success object (carrying the tables assigned) if the reservation is successfully inserted, a
     * Failure object otherwise
     */
    public ReservationAPIResult newReservation(ReservationDTO reservationDTO, Long layoutId) {
        initScheduleIfAbsent(
                reservationDTO.getStartDateTime(),
                reservationDTO.getEndDateTime()
        );

        CustomerAPIResult customerAPIResult = customerService.getCustomerById(reservationDTO.getCustomerId());
        if (!customerAPIResult.isSuccess())
            throw new NoSuchCustomerWithIdException(
                    reservationDTO.getCustomerId(),
                    NoSuchCustomerWithIdException.Cause.NO_SUCH_CUSTOMER_WITH_ID
            );

        Customer customer = (Customer) customerAPIResult.getSuccess().getResult();
        Reservation reservation = reservationMapper.toEntity(reservationDTO, customer);
        LayoutAPIResult layoutAPIResult = tableLayoutService.getLayoutById(layoutId);
        if (!layoutAPIResult.isSuccess()){
            throw new NoSuchLayoutWithIdException(+
                    layoutId,
                    NoSuchLayoutWithIdException.Cause.NO_SUCH_LAYOUT_WITH_ID
            );
        }
        SimpleMatrixLayout layout = (SimpleMatrixLayout) layoutAPIResult.getSuccess().getResult();

        Set<AbstractTable> reservationOutcome = reservationsService.newReservation(
                scheduleService,
                reservation,
                layout
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


    private void initScheduleIfAbsent(String startDateTime, String endDateTime) {
        scheduleService.initScheduleIfAbsent(
                tablesService,
                startDateTime,
                endDateTime
        );
    }


    /**
     * Given a set of reservation, returns a map which associates every partition of the set to the assigned table
     * @param allReservations a set of reservations.
     * @return a map
     */
    private HashMap<AbstractTable, Set<Reservation>> getAllReservationsMap(Set<Reservation> allReservations) {
        Set<AbstractTable> allTables = tablesService.getAllTables();
        return new HashMap<>(){{
            for (Reservation reservation : allReservations)
                for (AbstractTable jointTable : reservation.getJointTables())
                    computeIfAbsent(jointTable, unused -> new HashSet<>()).add(reservation);

            for (AbstractTable table : allTables){
                if (!containsKey(table))
                    put(table, new HashSet<>());
            }
        }};
    }


    public Customer getCustomerByPhoneNumber(String phoneNumber){
        return customerService.getCustomerByPhoneNumber(phoneNumber);
    }

    /**
     * Deletes a reservation giving its ID
     * @param reservationId A reservation ID
     * @return a Success object if the reservation is deleted successfully, a Failure otherwise
     */
    public ReservationAPIResult deleteReservation(Long reservationId) {
        Reservation reservationById = reservationsService.getReservationById(reservationId);
        return deleteReservation(reservationById);
    }

    /**
     * Deletes a reservation
     * @param reservation A reservation
     * @return a Success object if the reservation is deleted successfully, a Failure otherwise
     */
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

    /**
     * Tries to edit the number of people of a reservation
     * @param reservationId The reservation ID
     * @param newNumberOfPeople The new number of people
     * @return a Success if the reservation gets updated successfully, a Failure otherwise
     */
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

    /**
     * Performs the action bound to the parameter token. The association between token and
     *  action resides in the memory; only the confirmReschedule action is supported at the moment.
     * @param token a token previously generated
     * @return a Success if the action is successful, a Failure otherwise
     */
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
            // TODO: FIX
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

    /**
     * Triggers the creation of a "reschedule" action token bound to the reservation with the parameter id
     * and the newNumberOfPeople parameter.
     * @param reservationId
     * @param newNumberOfPeople
     * @return the generated token
     */
    public String triggerUpdateNumberOfPeopleTokenCreation(Long reservationId, Integer newNumberOfPeople) {
        Reservation reservation = reservationsService.getReservationById(reservationId);
        reservation.setNumberOfPeople(newNumberOfPeople);
        return createActionTokenBoundToReservation(CacheConstants.CONFIRM_RESCHEDULE, reservation);
    }

    /**
     * Creates an action token
     * @param action the action
     * @param reservation the reservation to be bound to the action
     * @return the generated token
     */
    private String createActionTokenBoundToReservation(String action, Reservation reservation) {
        return cacheUtils.createActionTokenBoundToObj(action, reservation);
    }


    /**
     * Returns a Reservation object specifying the ID
     * @param id A reservation ID
     * @return A Success (containing the reservation object) if a reservation with that ID exists, a Failure otherwise
     */
    public ReservationAPIResult getReservationById(Long id) {
        Reservation reservation = reservationsService.getReservationById(id);
        return (
                reservation == null
                ? new ReservationAPIResult.Failure(ReservationAPIError.NO_RESERVATION_WITH_ID)
                : new ReservationAPIResult.Success(reservation, ReservationAPIInfo.RESERVATION_FETCHED_OK)
        );
    }

    /**
     * Returns all Reservation object with an arrivalDateTime corresponding with the day passed as parameter
     * @param day A string representing the DateTime
     * @return a Success (containing a map that associates the reservations and the tables assigned) if there
     * are reservation for that day, a Failure otherwise
     */
    public ReservationAPIResult getAllReservationsByDay(String day) {
        Set<Reservation> reservationsByDay = reservationsService.getAllReservationsByDay(day);
        HashMap<AbstractTable, Set<Reservation>> res = getAllReservationsMap(reservationsByDay);
        return (
                res.isEmpty()
                        ? new ReservationAPIResult.Failure(ReservationAPIError.NO_RESERVATION_YET_FOR_DAY)
                        : new ReservationAPIResult.Success(res, ReservationAPIInfo.RESERVATION_FETCHED_OK)
        );
    }

    /**
     * Returns all today reservations.
     * @return a Success (containing a map that associates the reservations and the tables assigned) if there
     * are reservation for that day, a Failure otherwise
     * @see ReservationHandlingFacade#getAllReservationsByDay(String)
     */
    public ReservationAPIResult getAllTodayReservations() {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        return getAllReservationsByDay(date);
    }

    public ReservationAPIResult getScheduleCompliantTables(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        LocalDate startDate = startDateTime.toLocalDate();
        LocalDate endDate = endDateTime.toLocalDate();
        if (startDate.equals(endDate)) {
            getAllReservationsByDay(startDate.toString());

        }
        return null;
    }

}
