package it.matteobarbera.tablereservation.model.reservation;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ScheduleIdRecord implements Serializable{

    @Column(name = "table_id")
    private Long tableId;

    @Column(name = "date")
    private String parsedDate;

    @Column(name = "layout_id")
    private Long layoutId;

    public ScheduleIdRecord() {
    }

    public ScheduleIdRecord(Long tableId, String parsedDate, Long layoutId) {
        this.tableId = tableId;
        this.parsedDate = parsedDate;
        this.layoutId = layoutId;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ScheduleIdRecord that)) return false;
        return
                Objects.equals(tableId, that.tableId)
                        && Objects.equals(parsedDate, that.parsedDate)
                        && Objects.equals(layoutId, that.layoutId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableId, parsedDate, layoutId);
    }

    @Override
    public String toString() {
        String toString = "{"
                + "        \"tableId\":\"" + tableId + "\""
                + ",         \"parsedDate\":\"" + parsedDate + "\""
                + ",         \"layoutId\":\"" + layoutId + "\""
                + "}";
        return toString.replaceAll("[\n\r]", "   ");
    }
}

