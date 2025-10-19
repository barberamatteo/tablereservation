package it.matteobarbera.tablereservation.model.reservation.strategies.singletable;

import it.matteobarbera.tablereservation.model.reservation.Reservation;
import it.matteobarbera.tablereservation.service.reservation.ScheduleService;
import it.matteobarbera.tablereservation.model.table.AbstractTable;

import java.util.Set;

public interface SingleTableReservationStrategy {


    Set<AbstractTable> postReservation(ScheduleService scheduleService, Reservation reservation);

}
