package it.matteobarbera.tablereservation.model.reservation.strategies;

import it.matteobarbera.tablereservation.model.reservation.*;
import it.matteobarbera.tablereservation.model.table.AbstractTable;
import it.matteobarbera.tablereservation.model.table.CustomTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@Primary
public class FillLoungeFirst implements ReservationStrategy{

    private final ScheduleService scheduleService;
    private final ReservationsService reservationsService;

    @Autowired
    public FillLoungeFirst(
            ScheduleService scheduleService,
            ReservationsService reservationsService
    ) {
        this.scheduleService = scheduleService;
        this.reservationsService = reservationsService;
    }


    @Override
    public Set<AbstractTable> postReservation(Reservation reservation) {
        Set<Schedule> adequateSchedules = scheduleService.getSchedulesByDayAndAdequateTable(
                reservation.getInterval().getStartDateTime().toLocalDate(),
                reservation.getNumberOfPeople()
        );

        Set<AbstractTable> jointTables = new HashSet<>();

        for (Schedule schedule : adequateSchedules) {
            boolean conflictualReservation = false;

            for (Reservation r : schedule.getReservation()) {
                conflictualReservation =
                        conflictualReservation || reservation.getInterval().clashes(r.getInterval());

            }
                if (!conflictualReservation) {

                    jointTables.add(schedule.getId().getTable());
                    schedule.addReservation(reservation);
                    reservation.setSchedule(schedule);
                    scheduleService.addReservationToSchedule(schedule);
                    return jointTables;
                }
            }

        return jointTables;

    }


}
