package it.matteobarbera.tablereservation.model.reservation;

import it.matteobarbera.tablereservation.model.table.CustomTable;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "schedules")
@IdClass(ScheduleIdRecord.class)
public class Schedule {

    @Id
    @ManyToOne(optional = false)
    @JoinColumn(name = "table_id", nullable = false)
    private CustomTable table;

    @Id
    @Column(name = "date")
    private String parsedDate;


    @ManyToOne(optional = false)
    private Reservation reservation;

    public Schedule() {
    }

    public Schedule(CustomTable table, String parsedDate, Reservation reservation) {
        this.table = table;
        this.parsedDate = parsedDate;
        this.reservation = reservation;
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

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
}
