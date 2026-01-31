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
    public void persistTwoSchedules(Reservation reservation, Set<AbstractTable> tables, Set<Schedule> schedules) {
        List<Schedule> schedulePair = schedules.stream().toList();
        Schedule schedule1 = schedulePair.getFirst();
        Schedule schedule2 = schedulePair.get(1);

        tables.add(schedule1.getTable());

        reservation.setJointTables(tables);
        reservation.addSchedule(schedule1);
        reservation.addSchedule(schedule2);

        reservationsRepository.save(reservation);

        var splitReservation = splitReservation(reservation, Pair.of(schedule1, schedule2));

        schedule1.addReservation(splitReservation.getFirst());
        schedule2.addReservation(splitReservation.getSecond());

        scheduleService.updateSchedules(Set.of(schedule1, schedule2));
    }

    private static Pair<Reservation, Reservation> splitReservation(
            Reservation reservation,
            Pair<Schedule, Schedule> schedulePair
    ){
        Reservation firstHalf = new Reservation(reservation, new HashSet<>(){{add(schedulePair.getFirst());}});
        firstHalf.setEndDateTime(
                DateUtils.atMidnightMinusOne(firstHalf.getEndDate())
        );
        Reservation secondHalf = new Reservation(reservation, new HashSet<>(){{add(schedulePair.getSecond());}});
        secondHalf.setStartDateTime(
                DateUtils.atMidnight(secondHalf.getEndDate())
        );

        return Pair.of(firstHalf, secondHalf);
    }


}
