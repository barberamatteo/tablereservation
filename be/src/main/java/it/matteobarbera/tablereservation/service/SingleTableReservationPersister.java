package it.matteobarbera.tablereservation.service;

import it.matteobarbera.tablereservation.model.reservation.Reservation;
import it.matteobarbera.tablereservation.model.reservation.Schedule;
import it.matteobarbera.tablereservation.model.table.AbstractTable;
import it.matteobarbera.tablereservation.repository.reservation.ReservationsRepository;
import it.matteobarbera.tablereservation.service.reservation.ScheduleService;
import it.matteobarbera.tablereservation.utils.DateUtils;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class SingleTableReservationPersister implements Persister{


    private final ScheduleService scheduleService;
    private final ReservationsRepository reservationsRepository;

    public SingleTableReservationPersister(ScheduleService scheduleService, ReservationsRepository reservationsRepository) {
        this.scheduleService = scheduleService;
        this.reservationsRepository = reservationsRepository;
    }

    @Override
    public void persist(Reservation reservation, Set<AbstractTable> tables, Set<Schedule> schedules) {
        Schedule schedule = schedules.stream().findFirst().orElseThrow(RuntimeException::new);

        tables.add(schedule.getTable());
        reservation.setJointTables(tables);
        reservation.addSchedule(schedule);
        reservationsRepository.save(reservation);
        schedule.addReservation(reservation);
        scheduleService.updateSchedule(schedule);

    }

    @Override
    public void persistTwoSchedules(
            Reservation reservation,
            Set<AbstractTable> tables,
            Set<Pair<Schedule, Schedule>> schedules
    ) {
        Pair<Schedule, Schedule> schedulePair = schedules.stream().findFirst().orElseThrow(RuntimeException::new);
        Schedule schedule1 = schedulePair.getFirst();
        Schedule schedule2 = schedulePair.getSecond();

        tables.add(schedule1.getTable());

        reservation.setJointTables(tables);
        reservation.addSchedule(schedule1);
        reservation.addSchedule(schedule2);

        reservationsRepository.save(reservation);

        schedule1.addReservation(reservation);
        schedule2.addReservation(reservation);

        scheduleService.updateSchedules(Set.of(schedule1, schedule2));
    }




}
