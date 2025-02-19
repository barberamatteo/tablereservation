package it.matteobarbera.tablereservation.model.reservation.strategies;

import it.matteobarbera.tablereservation.model.reservation.*;
import it.matteobarbera.tablereservation.model.table.AbstractTable;
import it.matteobarbera.tablereservation.model.table.CustomTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
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
        List<Schedule> schedulesByDay = scheduleService.getSchedulesByDayAndAdequateTable(
                reservation.getInterval().getStartDateTime().toLocalDate(),
                reservation.getNumberOfPeople()
        );
        for (Schedule schedule : schedulesByDay) {
            if (schedule.getReservation().isEmpty()){
                scheduleService.addReservationToSchedule(
                        reservationsService.addReservation(
                                schedule,
                                reservation,
                                Set.of(schedule.getTable())),
                        schedule
                );
                return Set.of(schedule.getTable());
            }
            boolean conflictualReservation = false;
            for (Reservation r : schedule.getReservation()) {
                conflictualReservation =
                        conflictualReservation || reservation.getInterval().clashes(r.getInterval());
            }
            if (!conflictualReservation) {
                reservationsService.addReservation(
                        schedule,
                        reservation,
                        Set.of(schedule.getTable())
                );
                return Set.of(schedule.getTable());
            }
        }
        return Set.of();

    }


}
