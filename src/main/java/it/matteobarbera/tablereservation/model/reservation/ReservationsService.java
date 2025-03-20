package it.matteobarbera.tablereservation.model.reservation;

import it.matteobarbera.tablereservation.model.customer.CustomerService;
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

    public Set<AbstractTable> newReservation(
            CustomerService customerService,
            ScheduleService scheduleService,
            ReservationDTO reservationDTO
    ) {
        LocalDateTime startDateTime = LocalDateTime.parse(reservationDTO.getStartDateTime());
        LocalDateTime endDateTime;
        if (reservationDTO.getEndDateTime() == null) {
            endDateTime = startDateTime.plusMinutes(userPreferences.DEFAULT_LEAVE_TIME_MINUTES_OFFSET);
        } else {
            endDateTime = LocalDateTime.parse(reservationDTO.getEndDateTime());
        }

        Reservation reservation = new Reservation(
                startDateTime,
                endDateTime,
                customerService.getCustomerById(reservationDTO.getCustomerId()),
                reservationDTO.getNumberOfPeople()
        );

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
}
