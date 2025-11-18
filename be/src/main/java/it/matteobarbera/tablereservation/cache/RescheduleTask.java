package it.matteobarbera.tablereservation.cache;

import it.matteobarbera.tablereservation.model.reservation.Reservation;

public final class RescheduleTask extends AbstractTask{
    final Reservation reservation;
    final Integer newNumberOfPeople;
    public RescheduleTask(
            Reservation reservation,
            Integer newNumberOfPeople,
            Runnable task) {
        super(task);
        this.reservation = reservation;
        this.newNumberOfPeople = newNumberOfPeople;
    }

}
