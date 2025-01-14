package it.matteobarbera.tablereservation.model.reservation;

import it.matteobarbera.tablereservation.model.table.CustomTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface ReservationsRepository extends JpaRepository<Reservation, Long> {

    //Set<Reservation> getReservationsByDateAndJointTables(LocalDate date, Set<CustomTable> tables);
}
