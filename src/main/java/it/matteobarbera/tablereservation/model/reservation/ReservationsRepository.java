package it.matteobarbera.tablereservation.model.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface ReservationsRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r WHERE r.schedule.id.parsedDate = ?1")
    Set<Reservation> getAllByDate(String date);


}
