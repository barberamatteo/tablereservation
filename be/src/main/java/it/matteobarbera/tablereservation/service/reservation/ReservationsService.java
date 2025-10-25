package it.matteobarbera.tablereservation.service.reservation;

import it.matteobarbera.tablereservation.model.reservation.Reservation;
import it.matteobarbera.tablereservation.model.reservation.strategies.multitable.MultiTableReservationStrategy;
import it.matteobarbera.tablereservation.model.table.SimpleJoinableTable;
import it.matteobarbera.tablereservation.repository.reservation.ReservationsRepository;
import it.matteobarbera.tablereservation.model.preferences.UserPreferences;
import it.matteobarbera.tablereservation.model.reservation.strategies.singletable.SingleTableReservationStrategy;
import it.matteobarbera.tablereservation.model.table.AbstractTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
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

    public Set<AbstractTable> newReservation(ScheduleService scheduleService, Reservation reservation) {
        Set<AbstractTable> singleTableOutcome = singleTableReservationStrategy.postReservation(scheduleService, reservation);
        if (!singleTableOutcome.isEmpty())
            return singleTableOutcome;
        Set<AbstractTable> multiTableOutcome = multiTableReservationStrategy.postReservation(scheduleService, reservation);
        if (!multiTableOutcome.isEmpty())
            return multiTableOutcome;
        return Set.of();

    }


    public Reservation getReservationById(Long reservationId) {
        return reservationsRepository
                .findById(reservationId)
                .orElse(null);
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
}
