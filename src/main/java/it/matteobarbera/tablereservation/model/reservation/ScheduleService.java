package it.matteobarbera.tablereservation.model.reservation;

import it.matteobarbera.tablereservation.model.table.CustomTable;
import it.matteobarbera.tablereservation.model.table.admin.TablesService;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final TablesService tablesService;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository, TablesService tablesService) {
        this.scheduleRepository = scheduleRepository;
        this.tablesService = tablesService;
    }

    @Transactional
    public List<Schedule> getSchedulesByDayAndAdequateTable(LocalDate localDate, Integer numberOfPeople) {

        List<Schedule> schedules = scheduleRepository.getSchedulesByParsedDateAndAdequateTable(
                localDate.toString(),
                numberOfPeople
        );


        if (schedules.isEmpty()) {
            Set<CustomTable> adequateTables = tablesService.getAdequateTables(numberOfPeople);
            schedules = scheduleRepository.saveAll(
                    adequateTables.stream().map(
                            table -> new Schedule(table, localDate.toString())
                    ).toList()
            );

        }
        return schedules;
    }

    public void addReservationToSchedule(Reservation reservation, Schedule schedule) {
        schedule.addReservation(reservation);
        Schedule savedSchedule = scheduleRepository.save(schedule);
        List<Schedule> fetchedSchedule = scheduleRepository.getSchedulesByParsedDateAndAdequateTable("2025-01-07", 6);
    }
}
