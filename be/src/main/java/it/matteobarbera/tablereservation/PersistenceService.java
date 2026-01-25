package it.matteobarbera.tablereservation;

import it.matteobarbera.tablereservation.model.reservation.Reservation;
import it.matteobarbera.tablereservation.model.reservation.Schedule;
import it.matteobarbera.tablereservation.model.table.AbstractTable;
import it.matteobarbera.tablereservation.service.MultiTableReservationPersister;
import it.matteobarbera.tablereservation.service.SingleTableReservationPersister;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class PersistenceService {

    private final SingleTableReservationPersister singleTableReservationPersister;
    private final MultiTableReservationPersister multiTableReservationPersister;

    public PersistenceService(SingleTableReservationPersister singleTableReservationPersister, MultiTableReservationPersister multiTableReservationPersister) {
        this.singleTableReservationPersister = singleTableReservationPersister;
        this.multiTableReservationPersister = multiTableReservationPersister;
    }

    public void persistSingle(
            Reservation reservation,
            Set<AbstractTable> tables,
            Set<Schedule> schedules
    ){
        singleTableReservationPersister.persist(reservation, tables, schedules);
    }

    public void persistMulti(
            Reservation reservation,
            Set<AbstractTable> tables,
            Set<Schedule> schedules
    ){
        multiTableReservationPersister.persist(reservation, tables, schedules);
    }
}
