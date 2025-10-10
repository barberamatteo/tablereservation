package it.matteobarbera.tablereservation.model.reservation;

import it.matteobarbera.tablereservation.model.table.CustomTable;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table
public class Schedule {

    @EmbeddedId
    private ScheduleIdRecord id;


    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    private Set<Reservation> reservations = new HashSet<>();

    public Schedule() {
    }



    public Schedule(CustomTable table, String parsedDate) {
        this.id = new ScheduleIdRecord(table, parsedDate);
    }

    public ScheduleIdRecord getId() {
        return id;
    }

    public void setId(ScheduleIdRecord id) {
        this.id = id;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }


    public void addReservation(Reservation reservation) {
        this.reservations.add(reservation);
        reservation.setSchedule(this);
    }

    public boolean removeReservation(Reservation reservation) {
        return this.reservations.remove(reservation);
    }

    public void editReservation(Reservation reservation) {
        this.reservations.remove(reservation);
        this.reservations.add(reservation);
    }

    @Override
    public String toString() {
        String toString = "{"
                + "        \"id\":" + id
                + ",         \"reservation\":" + reservations
                + "}";
        return toString.replaceAll("[\n\r]", "   ");
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Schedule schedule)) return false;
        return Objects.equals(id, schedule.id) && Objects.equals(reservations, schedule.reservations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reservations);
    }
}
