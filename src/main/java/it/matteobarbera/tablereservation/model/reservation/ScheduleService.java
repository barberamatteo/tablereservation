package it.matteobarbera.tablereservation.model.reservation;

import it.matteobarbera.tablereservation.model.table.CustomTable;
import it.matteobarbera.tablereservation.model.table.admin.TablesService;
import it.matteobarbera.tablereservation.utils.DateUtils;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final TablesService tablesService;
    private final ReservationsService reservationsService;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository, TablesService tablesService, ReservationsService reservationsService) {
        this.scheduleRepository = scheduleRepository;
        this.tablesService = tablesService;
        this.reservationsService = reservationsService;
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
        System.out.println(schedule.getReservation().size());
        scheduleRepository.saveAndFlush(schedule);
    }

    public void initScheduleIfAbsent(String arrivalDateTime, String leaveDateTime) {
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
}
