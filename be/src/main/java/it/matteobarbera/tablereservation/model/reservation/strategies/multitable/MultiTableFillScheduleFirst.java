package it.matteobarbera.tablereservation.model.reservation.strategies.multitable;


import it.matteobarbera.tablereservation.model.reservation.Reservation;
import it.matteobarbera.tablereservation.model.reservation.Schedule;
import it.matteobarbera.tablereservation.model.table.SimpleJoinableTable;
import it.matteobarbera.tablereservation.service.reservation.ScheduleService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Primary
public class MultiTableFillScheduleFirst implements MultiTableReservationStrategy {


    @Override
    public Set<SimpleJoinableTable> postReservation(ScheduleService scheduleService, Reservation reservation) {
        Set<Schedule> intervalCompliantSchedules = scheduleService.getIntervalCompliantSchedules(
                reservation.getInterval()
        );
        return null;
    }
}
