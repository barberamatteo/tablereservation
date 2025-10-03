package it.matteobarbera.tablereservation.service.reservation;

import it.matteobarbera.tablereservation.model.reservation.Reservation;
import it.matteobarbera.tablereservation.repository.reservation.ReservationsRepository;
import it.matteobarbera.tablereservation.service.customer.CustomerService;
import it.matteobarbera.tablereservation.model.dto.ReservationDTO;
import it.matteobarbera.tablereservation.model.preferences.UserPreferences;
import it.matteobarbera.tablereservation.model.reservation.strategies.ReservationStrategy;
import it.matteobarbera.tablereservation.model.table.AbstractTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Service
public class ReservationsService {

    private final ReservationsRepository reservationsRepository;
    private final UserPreferences userPreferences;
    private final ReservationStrategy reservationStrategy;

    @Autowired
    public ReservationsService(ReservationsRepository reservationsRepository, UserPreferences userPreferences, ReservationStrategy reservationStrategy) {
        this.reservationsRepository = reservationsRepository;
        this.userPreferences = userPreferences;
        this.reservationStrategy = reservationStrategy;
    }


    public Set<Reservation> getAllTodayReservations() {
        return Set.copyOf(reservationsRepository.getAllByDate(
                        new SimpleDateFormat("yyyy-MM-dd").format(new Date())
                )
        );
    }

    public Set<AbstractTable> newReservation(ScheduleService scheduleService, Reservation reservation) {
        return reservationStrategy.postReservation(scheduleService, reservation);
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
