package it.matteobarbera.tablereservation.service.reservation;

import it.matteobarbera.tablereservation.model.reservation.Interval;
import it.matteobarbera.tablereservation.model.reservation.Reservation;
import it.matteobarbera.tablereservation.model.reservation.Schedule;
import it.matteobarbera.tablereservation.model.table.CustomTable;
import it.matteobarbera.tablereservation.service.table.TablesService;
import it.matteobarbera.tablereservation.repository.reservation.ScheduleRepository;
import it.matteobarbera.tablereservation.utils.DateUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Transactional
    public Set<Schedule> getSchedulesByDayAndAdequateTable(LocalDate localDate, Integer numberOfPeople) {
        return scheduleRepository.getSchedulesByParsedDateAndAdequateTable(
                localDate.toString(),
                numberOfPeople
        );

    }

    @Transactional
    public void addReservationToSchedule(Schedule schedule) {
        scheduleRepository.saveAndFlush(schedule);
    }

    public void initScheduleIfAbsent(TablesService tablesService, String arrivalDateTime, String leaveDateTime) {
        String arrivalDate = DateUtils.estrapolateDate(arrivalDateTime);
        String leaveDate = DateUtils.estrapolateDate(leaveDateTime);

        Set<CustomTable> allTables = tablesService.getAllTables();
        if (scheduleRepository.getSchedulesByParsedDate(arrivalDate).isEmpty()) {
            scheduleRepository.saveAll(
                    allTables.stream().map(table -> (new Schedule(table, arrivalDate))).toList()
            );
        }
        if (scheduleRepository.getSchedulesByParsedDate(leaveDate).isEmpty()) {
            scheduleRepository.saveAll(
                    allTables.stream().map(table -> (new Schedule(table, leaveDate))).toList()
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

    private void updateSchedule(Schedule scheduleOfReservation) {
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

    public Set<CustomTable> getIntervalCompliantTables(Interval interval) {
        Set<Schedule> schedules = getSchedulesByInterval(interval);
        Set<Schedule> compliantSchedules = filterIntervalCompliantSchedules(interval, schedules);
        return compliantSchedules
                .stream()
                .map(schedule -> schedule.getId().getTable()).collect(Collectors.toSet());
    }


}
