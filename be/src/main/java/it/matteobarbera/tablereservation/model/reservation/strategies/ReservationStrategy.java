package it.matteobarbera.tablereservation.model.reservation.strategies;

import it.matteobarbera.tablereservation.model.reservation.Manipulations;
import it.matteobarbera.tablereservation.model.reservation.Reservation;

public interface ReservationStrategy {

    Manipulations postReservation(Reservation reservation);
}
