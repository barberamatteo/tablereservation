package it.matteobarbera.tablereservation.service.reservation;

import it.matteobarbera.tablereservation.model.reservation.Interval;
import it.matteobarbera.tablereservation.model.reservation.Reservation;
import it.matteobarbera.tablereservation.model.reservation.Schedule;
import it.matteobarbera.tablereservation.model.table.AbstractTable;
import it.matteobarbera.tablereservation.model.table.layout.SimpleMatrixLayout;
import it.matteobarbera.tablereservation.service.table.TablesService;
import it.matteobarbera.tablereservation.repository.reservation.ScheduleRepository;
import it.matteobarbera.tablereservation.utils.DateUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public Schedule getScheduleById(LocalDate day, Long tableId){
        return scheduleRepository
                .getScheduleByRecordId(day, tableId)
                .orElse(null);
    }


    public Set<Schedule> getSchedulesByDayAndAdequateTable(LocalDate localDate, Integer numberOfPeople) {
        return scheduleRepository.getSchedulesByDateAndAdequateTable(
                localDate.toString(),
                numberOfPeople
        );
    }

    public Set<Pair<Schedule, Schedule>> getSchedulesByCoupleDayAndAdequateTable(
            LocalDate localDate,
            Integer numberOfPeople
    ){
        Set<Schedule> firstDaySchedules = new TreeSet<>(Comparator.comparingLong(
                schedule -> schedule.getTable().getNumberInLounge())
        );
        firstDaySchedules.addAll(
                getSchedulesByDayAndAdequateTable(
                        localDate,
                        numberOfPeople
                )
        );
        Set<Schedule> secondDaySchedules = new TreeSet<>(Comparator.comparingLong(
                schedule -> schedule.getTable().getNumberInLounge())
        );

        secondDaySchedules.addAll(
                getSchedulesByDayAndAdequateTable(
                        DateUtils.tomorrow(localDate),
                        numberOfPeople
                )
        );
        Set<Pair<Schedule, Schedule>> toRet = new HashSet<>();
        if (firstDaySchedules.size() != secondDaySchedules.size())
            throw new RuntimeException("The two schedules sets are not the same size");

        var iterator1 = firstDaySchedules.iterator();
        var iterator2 = secondDaySchedules.iterator();

        while (iterator1.hasNext() && iterator2.hasNext()){
            toRet.add(Pair.of(iterator1.next(), iterator2.next()));
        }
        return toRet;

    }


    @Transactional
    public void updateScheduleTransactional(Schedule schedule) {
        scheduleRepository.saveAndFlush(schedule);
    }

    public void updateSchedules(Set<Schedule> schedules){
        scheduleRepository.saveAll(schedules);
    }


    public void initScheduleIfAbsent(
            TablesService tablesService,
            String arrivalDateTime,
            String leaveDateTime,
            SimpleMatrixLayout layout
    ) {
        String arrivalDate = DateUtils.estrapolateDate(arrivalDateTime);
        String leaveDate = DateUtils.estrapolateDate(leaveDateTime);

        Set<AbstractTable> allTables = tablesService.getAllTables();
        if (scheduleRepository.getSchedulesByParsedDate(arrivalDate).isEmpty()) {
            scheduleRepository.saveAll(
                    allTables.stream().map(table -> (new Schedule(table, arrivalDate, layout))).collect(Collectors.toSet())
            );
        }
        if (scheduleRepository.getSchedulesByParsedDate(leaveDate).isEmpty()) {
            scheduleRepository.saveAll(
                    allTables.stream().map(table -> (new Schedule(table, leaveDate, layout))).collect(Collectors.toSet())
            );
        }
    }

    private Schedule getScheduleByReservation(Reservation reservation) {
        return scheduleRepository
                .getScheduleByReservationsContaining(reservation)
                .orElse(
                        null
                );
    }



    public void updateSchedule(Schedule scheduleOfReservation) {
        scheduleRepository.save(scheduleOfReservation);
    }

    public Boolean removeReservationFromSchedule(Reservation reservation) {
        Schedule scheduleOfReservation = getScheduleByReservation(reservation);
        if (scheduleOfReservation == null)
            return false;
        if (!scheduleOfReservation.removeReservation(reservation))
            return false;
        updateSchedule(scheduleOfReservation);
        return true;
    }

    public Boolean editReservationNumberOfPeopleInSchedule(Reservation reservation, Integer newNumberOfPeople) {
        Schedule scheduleOfReservation = getScheduleByReservation(reservation);
        if (scheduleOfReservation == null)
            return false;
        reservation.setNumberOfPeople(newNumberOfPeople);
        scheduleOfReservation.editReservation(reservation);
        updateSchedule(scheduleOfReservation);
        return true;
    }


    public Set<Schedule> getSchedulesByInterval(Interval interval) {
        String parsedStartDate = DateUtils.estrapolateDate(interval.getStartDateTime().toLocalDate());
        String parsedEndDate = DateUtils.estrapolateDate(interval.getEndDateTime().toLocalDate());

        final Set<Schedule> toRet = new HashSet<>(
                scheduleRepository.getSchedulesByParsedDate(parsedStartDate)
        );
        if (!parsedStartDate.equals(parsedEndDate)) {
            toRet.addAll(scheduleRepository.getSchedulesByParsedDate(parsedEndDate));
        }

        return toRet;
    }




    private Set<Schedule> filterIntervalCompliantSchedules(
            Interval interval,
            Set<Schedule> schedules
    ) {
        return schedules.stream().filter(schedule -> {
            for (Reservation reservation : schedule.getReservations()) {
                if (interval.clashes(reservation.getInterval())) {
                    return false;
                }
            }
            return true;
        }).collect(Collectors.toSet());
    }



    public Set<Schedule> getIntervalCompliantSchedules(Interval interval) {
        Set<Schedule> schedules = getSchedulesByInterval(interval);
        return filterIntervalCompliantSchedules(interval, schedules);
    }


    public Set<Schedule> getSchedulesOfTables(
            Set<AbstractTable> joinableTables,
            LocalDate startDate,
            SimpleMatrixLayout layout
    ) {
        return scheduleRepository.getSchedulesOfTables(
                joinableTables,
                DateUtils.estrapolateDate(startDate),
                layout
        );
    }
}
