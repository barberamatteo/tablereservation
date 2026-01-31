package it.matteobarbera.tablereservation.service;

import it.matteobarbera.tablereservation.model.reservation.Reservation;
import it.matteobarbera.tablereservation.model.reservation.Schedule;
import it.matteobarbera.tablereservation.model.table.AbstractTable;
import org.springframework.data.util.Pair;

import java.util.Set;

public interface Persister {
    void persist(
            Reservation reservation,
            Set<AbstractTable> tables,
            Set<Schedule> schedules
    );

    void persistTwoSchedules(
            Reservation reservation,
            Set<AbstractTable> tables,
            Set<Pair<Schedule, Schedule>> schedules
    );
}
