package it.matteobarbera.tablereservation.model.reservation;

import it.matteobarbera.tablereservation.model.table.CustomTable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ScheduleIdRecord implements Serializable{
    @ManyToOne(optional = false)
    @JoinColumn(name = "table_id", nullable = false)
    private CustomTable table;

    @Column(name = "date")
    private String parsedDate;

    public ScheduleIdRecord() {
    }

    public ScheduleIdRecord(CustomTable table, String parsedDate) {
        this.table = table;
        this.parsedDate = parsedDate;
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ScheduleIdRecord that)) return false;
        return Objects.equals(table, that.table) && Objects.equals(parsedDate, that.parsedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(table, parsedDate);
    }
}

