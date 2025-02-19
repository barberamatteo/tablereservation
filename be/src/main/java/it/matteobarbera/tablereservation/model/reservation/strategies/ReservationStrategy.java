package it.matteobarbera.tablereservation.model.reservation.strategies;

import it.matteobarbera.tablereservation.model.reservation.Reservation;
import it.matteobarbera.tablereservation.model.table.AbstractTable;
import it.matteobarbera.tablereservation.model.table.CustomTable;

import java.util.Set;

public interface ReservationStrategy {


    Set<AbstractTable> postReservation(Reservation reservation);

}
