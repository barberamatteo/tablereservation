package it.matteobarbera.tablereservation.model.reservation;

import it.matteobarbera.tablereservation.model.table.CustomTable;
import it.matteobarbera.tablereservation.model.table.admin.TablesService;
import it.matteobarbera.tablereservation.utils.DateUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

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

    public Schedule getScheduleByReservation(Reservation reservation) {
        return scheduleRepository
                .getScheduleByReservationContaining(reservation)
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
}
