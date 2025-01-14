package it.matteobarbera.tablereservation.model.reservation;

import it.matteobarbera.tablereservation.model.table.CustomTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;

@Service
public class ReservationsService {

    private final ReservationsRepository reservationsRepository;

    @Autowired
    public ReservationsService(ReservationsRepository reservationsRepository) {
        this.reservationsRepository = reservationsRepository;
    }

    public Reservation addReservation(Schedule schedule, Reservation reservation, Set<CustomTable> tables) {
        reservation.getJointTables().addAll(tables);
        reservation.setSchedule(schedule);
        return reservationsRepository.save(reservation);
    }

//    public Set<Reservation> getReservationsByDateAndJointTables(LocalDate date, Set<CustomTable> jointTables){
//        return reservationsRepository.getReservationsByDateAndJointTables(date, jointTables);
//    }
}
