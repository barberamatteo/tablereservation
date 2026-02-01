package it.matteobarbera.tablereservation.model.reservation.strategies.multitable;


import it.matteobarbera.tablereservation.PersistenceService;
import it.matteobarbera.tablereservation.model.reservation.Reservation;
import it.matteobarbera.tablereservation.model.reservation.Schedule;
import it.matteobarbera.tablereservation.model.table.AbstractTable;
import it.matteobarbera.tablereservation.model.table.SimpleJoinableTable;
import it.matteobarbera.tablereservation.model.table.layout.SimpleMatrixLayout;
import it.matteobarbera.tablereservation.model.table.layout.SubsetSumSolver;
import it.matteobarbera.tablereservation.service.reservation.ScheduleService;
import org.springframework.context.annotation.Primary;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Primary
public class MultiTableFillScheduleFirst implements MultiTableReservationStrategy {


    private final PersistenceService persistenceService;

    public MultiTableFillScheduleFirst(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public Set<AbstractTable> postReservation(
            ScheduleService scheduleService,
            Reservation reservation,
            SimpleMatrixLayout layout
    ) {
        if (reservation.getInterval().spansMoreDays())
            return postReservationMultiSchedule(scheduleService, reservation, layout);
        
        
        
        Set<Schedule> intervalCompliantSchedules = scheduleService.getIntervalCompliantSchedules(
                reservation.getInterval()
        );
        var joinableTables = extractJoinableTables(intervalCompliantSchedules);
        var subsetSumSolver = new SubsetSumSolver<>(joinableTables.stream().toList());
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
                persistenceService.persistMulti(
                        reservation,
                        joinedTables,
                        involvedSchedules
                );
                return joinedTables;
            }
        }
        return Set.of();

    }

    
    public Set<AbstractTable> postReservationMultiSchedule(
            ScheduleService scheduleService,
            Reservation reservation,
            SimpleMatrixLayout layout
    ){
        List<Pair<Schedule, Schedule>> adequateSchedulePairs = 
                scheduleService.getIntervalCompliantSchedulePairs(reservation.getInterval());
        Set<Schedule> intervalCompliantSchedules = new HashSet<>(){
            {
                for (var pair : adequateSchedulePairs){
                    add(pair.getFirst());
                }
            }
        };
        var joinableTables = extractJoinableTables(intervalCompliantSchedules);
        var subsetSumSolver = new SubsetSumSolver<>(joinableTables.stream().toList());
        var subsetsOfCapacities = subsetSumSolver.getSubsetsOfCapacities(reservation.getNumberOfPeople());
        for (var subset : subsetsOfCapacities){
            var joinedTables = layout.findBestPathWithExclusionsAndCapacities(
                    joinableTables,
                    subset
            );
            if (!joinedTables.isEmpty()) {
                var involvedSchedulesOfFirstDay = scheduleService.getSchedulesOfTables(
                        joinedTables,
                        reservation.getStartDate(),
                        layout
                );
                
                var involvedSchedulesOfSecondDay = scheduleService.getSchedulesOfTables(
                        joinedTables,
                        reservation.getEndDate(),
                        layout
                );
                Set<Schedule> involvedSchedules = new HashSet<>();
                involvedSchedules.addAll(involvedSchedulesOfFirstDay);
                involvedSchedules.addAll(involvedSchedulesOfSecondDay);

                persistenceService.persistMulti(
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
