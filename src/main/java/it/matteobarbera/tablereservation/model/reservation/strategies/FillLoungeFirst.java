package it.matteobarbera.tablereservation.model.reservation.strategies;

import it.matteobarbera.tablereservation.model.reservation.Manipulations;
import it.matteobarbera.tablereservation.model.reservation.Reservation;
import it.matteobarbera.tablereservation.model.reservation.ReservationsService;
import it.matteobarbera.tablereservation.model.reservation.ScheduleService;
import it.matteobarbera.tablereservation.model.table.CustomTable;
import it.matteobarbera.tablereservation.model.table.admin.TablesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class FillLoungeFirst implements ReservationStrategy{

    private final TablesService tablesService;
    private final ScheduleService scheduleService;
    private final ReservationsService reservationsService;

    @Autowired
    public FillLoungeFirst(TablesService tablesService, ScheduleService scheduleService, ReservationsService reservationsService) {
        this.tablesService = tablesService;
        this.scheduleService = scheduleService;
        this.reservationsService = reservationsService;
    }
    @Override
    public synchronized Manipulations postReservation(Reservation reservation) {
        return new Manipulations(){{
           add(() -> {
                 Set<CustomTable> adequateTables = tablesService.getAdequateTables(reservation);
                 if (!adequateTables.isEmpty()) {
                     for (CustomTable table : adequateTables) {
                         if (!scheduleService.conflictsWith(table, reservation))
                             reservationsService.addReservation(reservation);
                     }
                 }

                 return null;
           });
        }};

    }
}
