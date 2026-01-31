package it.matteobarbera.tablereservation.model.reservation.strategies.singletable;

import it.matteobarbera.tablereservation.PersistenceService;
import it.matteobarbera.tablereservation.model.reservation.Reservation;
import it.matteobarbera.tablereservation.model.reservation.Schedule;
import it.matteobarbera.tablereservation.service.reservation.ScheduleService;
import it.matteobarbera.tablereservation.model.table.AbstractTable;
import org.springframework.context.annotation.Primary;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Primary
public class SingleTableFillScheduleFirst implements SingleTableReservationStrategy {

    private final PersistenceService persistenceService;

    public SingleTableFillScheduleFirst(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public Set<AbstractTable> postReservation(ScheduleService scheduleService, Reservation reservation) {
        if (reservation.getInterval().spansMoreDays())
            return postReservationMultiSchedule(scheduleService, reservation);
        List<Schedule> adequateSchedules = scheduleService.getSchedulesByDayAndAdequateTable(
                reservation.getStartDate(),
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
                    persistenceService.persistSingle(
                            reservation,
                            jointTables,
                            Set.of(schedule)
                    );
                    return jointTables;
                }
            }

        return jointTables;

    }

    private Set<AbstractTable> postReservationMultiSchedule(ScheduleService scheduleService, Reservation reservation) {
        List<Pair<Schedule, Schedule>> adequateSchedulesPairs = scheduleService.getSchedulesByCoupleDayAndAdequateTable(
                reservation.getStartDate(),
                reservation.getNumberOfPeople()
        );

        Set<AbstractTable> jointTables = new HashSet<>();

        for (Pair<Schedule, Schedule> schedulesPair : adequateSchedulesPairs) {
            boolean firstDayConflictualReservation = false;
            boolean secondDayConflictualReservation = false;
            for (Reservation r : schedulesPair.getFirst().getReservations()) {
                firstDayConflictualReservation =
                        firstDayConflictualReservation || reservation.getInterval().clashes(r.getInterval());
            }

            for (Reservation r : schedulesPair.getSecond().getReservations()) {
                secondDayConflictualReservation =
                        secondDayConflictualReservation || reservation.getInterval().clashes(r.getInterval());
            }


            boolean conflictualReservation = firstDayConflictualReservation || secondDayConflictualReservation;


            if (!conflictualReservation) {
                persistenceService.persistSingleWithinTwoSchedules(
                        reservation,
                        jointTables,
                        Set.of(Pair.of(schedulesPair.getFirst(), schedulesPair.getSecond()))
                );
                return jointTables;
            }
        }


        return jointTables;
    }


}
