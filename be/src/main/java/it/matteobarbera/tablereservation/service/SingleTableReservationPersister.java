package it.matteobarbera.tablereservation.service;

import it.matteobarbera.tablereservation.model.reservation.Reservation;
import it.matteobarbera.tablereservation.model.reservation.Schedule;
import it.matteobarbera.tablereservation.model.table.AbstractTable;
import it.matteobarbera.tablereservation.service.reservation.ScheduleService;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;

@Component
public class SingleTableReservationPersister implements Persister{


    private final ScheduleService scheduleService;

    public SingleTableReservationPersister(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @Override
    public void persist(Reservation reservation, Set<AbstractTable> tables, Set<Schedule> schedules) {
        Schedule schedule = schedules.stream().findFirst().orElseThrow(RuntimeException::new);

        tables.add(schedule.getTable());
        reservation.setJointTables(tables);
        schedule.addReservation(reservation);
        reservation.setSchedules(Set.of(schedule));
        scheduleService.updateScheduleTransactional(schedule);

    }
}
