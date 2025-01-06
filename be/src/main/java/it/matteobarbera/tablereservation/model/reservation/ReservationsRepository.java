package it.matteobarbera.tablereservation.model.reservation;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationsRepository extends JpaRepository<Reservation, Long> {

}
