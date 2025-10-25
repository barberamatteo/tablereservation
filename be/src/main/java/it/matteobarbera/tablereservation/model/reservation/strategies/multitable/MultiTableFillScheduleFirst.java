package it.matteobarbera.tablereservation.model.reservation.strategies.multitable;


import it.matteobarbera.tablereservation.model.reservation.Reservation;
import it.matteobarbera.tablereservation.model.reservation.Schedule;
import it.matteobarbera.tablereservation.model.table.AbstractTable;
import it.matteobarbera.tablereservation.model.table.SimpleJoinableTable;
import it.matteobarbera.tablereservation.service.reservation.ScheduleService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@Primary
public class MultiTableFillScheduleFirst implements MultiTableReservationStrategy {


    @Override
    public Set<AbstractTable> postReservation(ScheduleService scheduleService, Reservation reservation) {
        Set<Schedule> intervalCompliantSchedules = scheduleService.getIntervalCompliantSchedules(
                reservation.getInterval()
        );
        Set<SimpleJoinableTable> joinableTables = extractJoinableTables(intervalCompliantSchedules);


        return null;
    }

    private Set<SimpleJoinableTable> extractJoinableTables(Set<Schedule> schedules) {
        Set<SimpleJoinableTable> toRet = new HashSet<>();
        for (Schedule schedule : schedules) {
            if (schedule.getId().getTable() instanceof SimpleJoinableTable table) {
                toRet.add(table);
            }
        }
        return toRet;
    }
}
