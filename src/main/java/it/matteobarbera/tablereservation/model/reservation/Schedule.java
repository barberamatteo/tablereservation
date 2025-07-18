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
    private Set<Reservation> reservation = new HashSet<>();

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

    public Set<Reservation> getReservation() {
        return reservation;
    }

    public void setReservation(Set<Reservation> reservations) {
        this.reservation = reservations;
    }


    public void addReservation(Reservation reservation) {
        this.reservation.add(reservation);
        reservation.setSchedule(this);
    }

    public boolean removeReservation(Reservation reservation) {
        return this.reservation.remove(reservation);
    }

    public void editReservation(Reservation reservation) {
        this.reservation.remove(reservation);
        this.reservation.add(reservation);
    }

    @Override
    public String toString() {
        String toString = "{"
                + "        \"id\":" + id
                + ",         \"reservation\":" + reservation
                + "}";
        return toString.replaceAll("[\n\r]", "   ");
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Schedule schedule)) return false;
        return Objects.equals(id, schedule.id) && Objects.equals(reservation, schedule.reservation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reservation);
    }
}
