package it.matteobarbera.tablereservation.model.reservation;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

import java.time.LocalDateTime;

@Entity
@IdClass(IntervalIdRecord.class)

public class Interval {
    @Id
    private LocalDateTime startDateTime;
    @Id
    private LocalDateTime endDateTime;

    public Interval() {
    }

    public Interval(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public Interval(String startDateTime, String endDateTime) {
        this(LocalDateTime.parse(startDateTime), LocalDateTime.parse(endDateTime));
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime end) {
        this.endDateTime = end;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime start) {
        this.startDateTime = start;
    }

    public boolean clashes(Interval other){
        return
                !this.endDateTime.isEqual(other.startDateTime)
                        &&
                !other.endDateTime.isEqual(this.startDateTime)
                        &&
                !this.endDateTime.isBefore(other.startDateTime)
                        &&
                !other.endDateTime.isBefore(this.startDateTime);
    }

    @Override
    public String toString() {
        String sdd = String.valueOf(this.startDateTime.getDayOfMonth());
        String sMM = String.valueOf(this.startDateTime.getMonth().getValue());
        String syyyy = String.valueOf(this.startDateTime.getYear());
        String shh = String.valueOf(this.startDateTime.getHour());
        String smm = String.valueOf(this.startDateTime.getMinute());

        String edd = String.valueOf(this.endDateTime.getDayOfMonth());
        String eMM = String.valueOf(this.endDateTime.getMonth().getValue());
        String eyyyy = String.valueOf(this.endDateTime.getYear());
        String ehh = String.valueOf(this.endDateTime.getHour());
        String emm = String.valueOf(this.endDateTime.getMinute());

        return "[" +
                (sdd.length() == 1 ? "0" + sdd : sdd) + "/" +
                (sMM.length() == 1 ? "0" + sMM : sMM) + "/" +
                syyyy + " at " +
                (shh.length() == 1 ? "0" + shh : shh) + ":" + (smm.length() == 1 ? "0" + smm : smm) + ", "
                +
                (edd.length() == 1 ? "0" + edd : edd) + "/" +
                (eMM.length() == 1 ? "0" + eMM : eMM) + "/" +
                eyyyy + " at " +
                (ehh.length() == 1 ? "0" + ehh : ehh) + ":" + (emm.length() == 1 ? "0" + emm : emm) +
                "]";
    }
}
