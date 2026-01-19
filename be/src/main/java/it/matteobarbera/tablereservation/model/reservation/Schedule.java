package it.matteobarbera.tablereservation.model.reservation;

import it.matteobarbera.tablereservation.model.table.AbstractTable;
import it.matteobarbera.tablereservation.model.table.layout.SimpleMatrixLayout;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table
public class Schedule {

    @EmbeddedId
    public ScheduleIdRecord id;

    @MapsId("tableId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "table_id", nullable = false)
    private AbstractTable table;


    @MapsId("layoutId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "layout_id", nullable = false)
    private SimpleMatrixLayout layout;

    @ManyToMany
    private Set<Reservation> reservations;

    public Schedule() {
    }



    public Schedule(AbstractTable table, String parsedDate, SimpleMatrixLayout layout) {
        this.table = table;
        this.layout = layout;
        this.id = new ScheduleIdRecord(table.getId(), parsedDate, layout.getId());
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

    public AbstractTable getTable() {
        return table;
    }

    public void setTable(AbstractTable table) {
        this.table = table;
    }

    public SimpleMatrixLayout getLayout() {
        return layout;
    }

    public void setLayout(SimpleMatrixLayout layout) {
        this.layout = layout;
    }

    public void addReservation(Reservation reservation) {
        this.reservations.add(reservation);
        reservation.addSchedule(this);
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
