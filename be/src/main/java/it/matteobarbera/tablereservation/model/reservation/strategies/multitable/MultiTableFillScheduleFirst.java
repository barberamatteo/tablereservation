package it.matteobarbera.tablereservation.model.reservation.strategies.multitable;


import it.matteobarbera.tablereservation.model.reservation.Reservation;
import it.matteobarbera.tablereservation.model.reservation.Schedule;
import it.matteobarbera.tablereservation.model.table.AbstractTable;
import it.matteobarbera.tablereservation.model.table.SimpleJoinableTable;
import it.matteobarbera.tablereservation.model.table.layout.SimpleMatrixLayout;
import it.matteobarbera.tablereservation.model.table.layout.SubsetSumSolver;
import it.matteobarbera.tablereservation.service.MultiTableReservationPersister;
import it.matteobarbera.tablereservation.service.reservation.ScheduleService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@Primary
public class MultiTableFillScheduleFirst implements MultiTableReservationStrategy {


    private final MultiTableReservationPersister multiTableReservationPersister;

    public MultiTableFillScheduleFirst(MultiTableReservationPersister multiTableReservationPersister) {
        this.multiTableReservationPersister = multiTableReservationPersister;
    }

    @Override
    public Set<AbstractTable> postReservation(
            ScheduleService scheduleService,
            Reservation reservation,
            SimpleMatrixLayout layout
    ) {
        Set<Schedule> intervalCompliantSchedules = scheduleService.getIntervalCompliantSchedules(
                reservation.getInterval()
        );
        var joinableTables = extractJoinableTables(intervalCompliantSchedules);
        var subsetSumSolver = new SubsetSumSolver<>(joinableTables.stream().toList());
//        var subsetOfCapacities = subsetSumSolver.getBestSubsetOfCapacities(reservation.getNumberOfPeople());
        var subsetsOfCapacities = subsetSumSolver.getSubsetsOfCapacities(reservation.getNumberOfPeople());
        for (var subset : subsetsOfCapacities){
            var joinedTables = layout.findBestPathWithExclusionsAndCapacities(
                    joinableTables,
                    subset
            );
            if (!joinedTables.isEmpty()) {
                var involvedSchedules = scheduleService.getSchedulesOfTables(
                        joinedTables,
                        reservation.getStartDate(),
                        layout
                );
                multiTableReservationPersister.persist(
                        reservation,
                        joinedTables,
                        involvedSchedules
                );
                return joinedTables;
            }
        }
        return Set.of();

    }

    private Set<SimpleJoinableTable> extractJoinableTables(Set<Schedule> schedules) {
        Set<SimpleJoinableTable> toRet = new HashSet<>();
        for (Schedule schedule : schedules) {
            if (schedule.getTable() instanceof SimpleJoinableTable table) {
                toRet.add(table);
            }
        }
        return toRet;
    }
}
