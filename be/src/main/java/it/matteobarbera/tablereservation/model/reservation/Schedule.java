package it.matteobarbera.tablereservation.model.reservation;

import it.matteobarbera.tablereservation.model.table.CustomTable;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@IdClass(ScheduleIdRecord.class)
public class Schedule {

    @Id
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "table_id", nullable = false)
    private CustomTable table;

    @Id
    @Column(name = "date")
    private String parsedDate;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "schedule")
    private List<Reservation> reservation;

    public Schedule() {
    }

    public Schedule(CustomTable table, String parsedDate, List<Reservation> reservation) {
        this.table = table;
        this.parsedDate = parsedDate;
        this.reservation = reservation;
    }

    public Schedule(CustomTable table, String parsedDate) {
        this.table = table;
        this.parsedDate = parsedDate;
        this.reservation = new ArrayList<>();
    }
    public CustomTable getTable() {
        return table;
    }

    public void setTable(CustomTable table) {
        this.table = table;
    }

    public String getParsedDate() {
        return parsedDate;
    }

    public void setParsedDate(String parsedDate) {
        this.parsedDate = parsedDate;
    }

    public List<Reservation> getReservation() {
        return reservation;
    }

    public void setReservation(List<Reservation> reservations) {
        this.reservation = reservations;
    }

    public void addReservation(Reservation reservation) {
        this.reservation.add(reservation);
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "table=" + table +
                ", parsedDate='" + parsedDate + '\'' +
                ", reservationsSize=" + reservation.size() +
                '}';
    }
}
