package it.matteobarbera.tablereservation.model.reservation;

import it.matteobarbera.tablereservation.model.table.CustomTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }



    public Boolean conflictsWith(CustomTable table, Reservation reservation) {
        List<Schedule> schedules = scheduleRepository.getScheduleByParsedDateAndTable(
                reservation.getInterval().getStartDateTime().toLocalDate().toString(),
                table
        );

        for (Schedule schedule : schedules) {
            if (schedule.getReservation().getInterval().clashes(reservation.getInterval()))
                return true;
        }

        return false;

    }

}
