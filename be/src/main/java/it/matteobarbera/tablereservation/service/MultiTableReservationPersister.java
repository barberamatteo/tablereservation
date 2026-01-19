package it.matteobarbera.tablereservation.service;

import it.matteobarbera.tablereservation.model.reservation.Reservation;
import it.matteobarbera.tablereservation.model.reservation.Schedule;
import it.matteobarbera.tablereservation.model.table.AbstractTable;
import it.matteobarbera.tablereservation.service.reservation.ReservationsService;
import it.matteobarbera.tablereservation.service.reservation.ScheduleService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class MultiTableReservationPersister implements Persister {

    private final ReservationsService reservationsService;
    private final ScheduleService scheduleService;

    public MultiTableReservationPersister(@Lazy ReservationsService reservationsService, ScheduleService scheduleService) {
        this.reservationsService = reservationsService;
        this.scheduleService = scheduleService;
    }

    @Override
    public void persist(Reservation reservation, Set<AbstractTable> tables, Set<Schedule> schedules) {
        reservation.setJointTables(tables);
        reservation.setSchedules(schedules);
        reservationsService.saveReservation(reservation);
        for (Schedule schedule : schedules){
            schedule.addReservation(reservation);
        }
        //schedules.forEach(schedule -> schedule.addReservation(reservation));
        scheduleService.updateSchedules(schedules);
    }
}
