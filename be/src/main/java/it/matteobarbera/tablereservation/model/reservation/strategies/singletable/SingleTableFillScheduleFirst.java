package it.matteobarbera.tablereservation.model.reservation.strategies.singletable;

import it.matteobarbera.tablereservation.model.reservation.Reservation;
import it.matteobarbera.tablereservation.model.reservation.Schedule;
import it.matteobarbera.tablereservation.service.SingleTableReservationPersister;
import it.matteobarbera.tablereservation.service.reservation.ScheduleService;
import it.matteobarbera.tablereservation.model.table.AbstractTable;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@Primary
public class SingleTableFillScheduleFirst implements SingleTableReservationStrategy {


    private final SingleTableReservationPersister singleTableReservationPersister;

    public SingleTableFillScheduleFirst(SingleTableReservationPersister singleTableReservationPersister) {
        this.singleTableReservationPersister = singleTableReservationPersister;
    }


    @Override
    public Set<AbstractTable> postReservation(ScheduleService scheduleService, Reservation reservation) {
        Set<Schedule> adequateSchedules = scheduleService.getSchedulesByDayAndAdequateTable(
                reservation.getInterval().getStartDateTime().toLocalDate(),
                reservation.getNumberOfPeople()
        );

        Set<AbstractTable> jointTables = new HashSet<>();

        for (Schedule schedule : adequateSchedules) {
            boolean conflictualReservation = false;

            for (Reservation r : schedule.getReservations()) {
                conflictualReservation =
                        conflictualReservation || reservation.getInterval().clashes(r.getInterval());

            }
                if (!conflictualReservation) {
                    singleTableReservationPersister.persist(
                            reservation,
                            jointTables,
                            Set.of(schedule)
                    );
                    return jointTables;
                }
            }

        return jointTables;

    }


}
