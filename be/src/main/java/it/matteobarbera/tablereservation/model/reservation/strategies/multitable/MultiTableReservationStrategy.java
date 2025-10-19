package it.matteobarbera.tablereservation.model.reservation.strategies.multitable;

import it.matteobarbera.tablereservation.model.reservation.Reservation;
import it.matteobarbera.tablereservation.model.table.SimpleJoinableTable;
import it.matteobarbera.tablereservation.service.reservation.ScheduleService;

import java.util.Set;

public interface MultiTableReservationStrategy {

    Set<SimpleJoinableTable> postReservation(ScheduleService scheduleService, Reservation reservation);
}
