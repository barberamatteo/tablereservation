package it.matteobarbera.tablereservation.orchestrator;

import it.matteobarbera.tablereservation.cache.*;
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
import it.matteobarbera.tablereservation.service.table.TablesService;
import it.matteobarbera.tablereservation.service.table.layout.TableLayoutService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Orchestrator component that orchestrates all the domain services in the process of handling the HTTP calls to the
 * Reservations API endpoints, in order to achieve low coupling between services and controllers.
 */
@Component
public class ReservationHandlingOrchestrator {

    private final CustomerService customerService;
    private final ScheduleService scheduleService;
    private final TablesService tablesService;
    private final ReservationsService reservationsService;
    private final Cache cache;
    private final ReservationMapper reservationMapper;
    private final TableLayoutService tableLayoutService;

    public ReservationHandlingOrchestrator(
            CustomerService customerService,
            ScheduleService scheduleService,
            TablesService tablesService,
            ReservationsService reservationsService,
            Cache cache,
            ReservationMapper reservationMapper,
            TableLayoutService tableLayoutService) {
        this.customerService = customerService;
        this.scheduleService = scheduleService;
        this.tablesService = tablesService;
        this.reservationsService = reservationsService;
        this.cache = cache;
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
            throw new NoSuchLayoutWithIdException(
                    layoutId,
                    NoSuchLayoutWithIdException.Cause.NO_SUCH_LAYOUT_WITH_ID
            );
        }
        SimpleMatrixLayout layout = (SimpleMatrixLayout) layoutAPIResult.getSuccess().getResult();

        initScheduleIfAbsent(
                reservationDTO.getStartDateTime(),
                reservationDTO.getEndDateTime(),
                layout
        );
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


    private void initScheduleIfAbsent(String startDateTime, String endDateTime, SimpleMatrixLayout layout) {
        scheduleService.initScheduleIfAbsent(tablesService, startDateTime, endDateTime, layout);
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

    /**
     * Deletes a reservation giving its ID
     * @param reservationId A reservation ID
     * @return a Success object if the reservation is deleted successfully, a Failure otherwise
     */
    public ReservationAPIResult deleteReservation(Long reservationId) {
        ReservationAPIResult reservationAPIResult = reservationsService.getReservationById(reservationId);
        if (reservationAPIResult.isSuccess()) {
            Reservation reservationToDelete = reservationAPIResult.getSuccess().get();
            return deleteReservation(reservationToDelete);
        } else {
            return new ReservationAPIResult.Failure(ReservationAPIError.NO_RESERVATION_WITH_ID);
        }
    }

    /**
     * Deletes a reservation
     * @param reservation A reservation
     * @return a Success object if the reservation is deleted successfully, a Failure otherwise
     */
    public ReservationAPIResult deleteReservation(Reservation reservation) {
        if (reservation == null)
            return new ReservationAPIResult.Failure(ReservationAPIError.GENERAL_ERROR);
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
        ReservationAPIResult reservationByIdApiResult = reservationsService.getReservationById(reservationId);
        if (reservationByIdApiResult.getStatus() == ReservationAPIError.NO_RESERVATION_WITH_ID)
            return reservationByIdApiResult;

        if (reservationByIdApiResult.isSuccess()) {
            Reservation reservation = reservationByIdApiResult.getSuccess().get();
            if (reservationsService.isNumberOfPeopleUpdatableWithoutRescheduling(reservation, newNumberOfPeople)) {
                return (
                        scheduleService.editReservationNumberOfPeopleInSchedule(reservation, newNumberOfPeople)
                            ? new ReservationAPIResult.Success(ReservationAPIInfo.RESERVATION_UPDATE_OK)
                            : new ReservationAPIResult.Failure(ReservationAPIError.NO_RESERVATION_WITH_ID_IN_SCHEDULE)
                );
            } else {
                return new ReservationAPIResult.Failure(ReservationAPIError.NEED_TO_RESCHEDULE);
            }
        } else {
            return new ReservationAPIResult.Failure(ReservationAPIError.GENERAL_ERROR);
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
        ActionCacheEntry<?> cacheQuery = cache.getTaskFromToken(token);
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
            ReservationAPIResult recreateResult = newReservation(
                    ReservationDTO.from(reservation),
                    null/*reservation.getSchedule().getLayout().getId()*/
            );
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
     * Triggers the creation of a "reschedule" task token bound to the reservation with the parameter id
     * and the newNumberOfPeople parameter.
     * @param reservationId
     * @param newNumberOfPeople
     * @return the generated token
     */
    public ReservationAPIResult triggerUpdateNumberOfPeopleTokenCreation(
            Long reservationId,
            Integer newNumberOfPeople
    ) {
        ReservationAPIResult reservationAPIResult = reservationsService.getReservationById(reservationId);
        if (reservationAPIResult.isSuccess()) {
            Reservation reservation = reservationAPIResult.getSuccess().get();
            reservation.setNumberOfPeople(newNumberOfPeople);
            AbstractTask rescheduleTask = new RescheduleTask(reservation, newNumberOfPeople, () -> {

            });

            String token = createTokenBoundToTask(rescheduleTask);
            return new ReservationAPIResult.Success(token, ReservationAPIInfo.TOKEN_CREATED_OK);
        } else {
            return new ReservationAPIResult.Failure(ReservationAPIError.NO_RESERVATION_WITH_ID);
        }
    }

    /**
     * Creates a task token
     * @param task the task object
     * @return the generated token
     */
    private String createTokenBoundToTask(AbstractTask task) {
        return cache.createTokenBoundToTask(task);
    }


    /**
     * Returns a Reservation object specifying the ID
     * @param id A reservation ID
     * @return A Success (containing the reservation object) if a reservation with that ID exists, a Failure otherwise
     */
    public ReservationAPIResult getReservationById(Long id) {
        return reservationsService.getReservationById(id);
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
     * @see ReservationHandlingOrchestrator#getAllReservationsByDay(String)
     */
    public ReservationAPIResult getAllTodayReservations() {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        return getAllReservationsByDay(date);
    }

}
