package it.matteobarbera.tablereservation.service.reservation;

import it.matteobarbera.tablereservation.http.ReservationAPIError;
import it.matteobarbera.tablereservation.http.ReservationAPIInfo;
import it.matteobarbera.tablereservation.http.ReservationAPIResult;
import it.matteobarbera.tablereservation.model.reservation.Reservation;
import it.matteobarbera.tablereservation.model.reservation.strategies.multitable.MultiTableReservationStrategy;
import it.matteobarbera.tablereservation.model.table.SimpleJoinableTable;
import it.matteobarbera.tablereservation.model.table.layout.SimpleMatrixLayout;
import it.matteobarbera.tablereservation.repository.reservation.ReservationsRepository;
import it.matteobarbera.tablereservation.model.preferences.UserPreferences;
import it.matteobarbera.tablereservation.model.reservation.strategies.singletable.SingleTableReservationStrategy;
import it.matteobarbera.tablereservation.model.table.AbstractTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class ReservationsService {

    private final ReservationsRepository reservationsRepository;
    private final UserPreferences userPreferences;
    private final SingleTableReservationStrategy singleTableReservationStrategy;
    private final MultiTableReservationStrategy multiTableReservationStrategy;

    @Autowired
    public ReservationsService(
            ReservationsRepository reservationsRepository,
            UserPreferences userPreferences,
            SingleTableReservationStrategy singleTableReservationStrategy,
            MultiTableReservationStrategy multiTableReservationStrategy) {
        this.reservationsRepository = reservationsRepository;
        this.userPreferences = userPreferences;
        this.singleTableReservationStrategy = singleTableReservationStrategy;
        this.multiTableReservationStrategy = multiTableReservationStrategy;
    }


    public Set<Reservation> getAllTodayReservations() {
        return Set.copyOf(reservationsRepository.getAllByDate(
                        new SimpleDateFormat("yyyy-MM-dd").format(new Date())
                )
        );
    }


    // TODO: TEMP
    public Set<AbstractTable> newReservation(
            ScheduleService scheduleService,
            Reservation reservation,
            SimpleMatrixLayout layout
    ) {
        Set<AbstractTable> singleTableOutcome = singleTableReservationStrategy.postReservation(scheduleService, reservation);
        if (!singleTableOutcome.isEmpty())
            return singleTableOutcome;
        Set<AbstractTable> multiTableOutcome = multiTableReservationStrategy.postReservation(
                scheduleService,
                reservation,
                layout
        );
        if (!multiTableOutcome.isEmpty())
            return multiTableOutcome;
        return Set.of();
    }






    public ReservationAPIResult getReservationById(Long reservationId) {
        var result = reservationsRepository.findById(reservationId);
        if (result.isPresent()) {
            return new ReservationAPIResult.Success(result, ReservationAPIInfo.RESERVATION_FETCHED_OK);
        } else {
            return new ReservationAPIResult.Failure(ReservationAPIError.NO_RESERVATION_WITH_ID);
        }
    }

    public Boolean isNumberOfPeopleUpdatableWithoutRescheduling(Reservation reservation, Integer numberOfPeople) {
        return
                reservation.getJointTables().stream().mapToInt(customTable ->
                        customTable.getTableDefinition().getStandaloneCapacity()
                ).sum()
                        >= numberOfPeople;
    }

    public void deleteReservation(Reservation reservation) {
        reservationsRepository.delete(reservation);
    }

    public Set<Reservation> getAllReservationsByDay(String day) {
        return Set.copyOf(reservationsRepository.getAllByDate(day));
    }

    public void saveReservation(Reservation reservation) {
        reservationsRepository.save(reservation);
    }
}
