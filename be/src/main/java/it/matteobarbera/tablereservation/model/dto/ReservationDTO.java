package it.matteobarbera.tablereservation.model.dto;

import it.matteobarbera.tablereservation.model.reservation.Reservation;
import org.springframework.lang.NonNull;

import java.util.Objects;

public class ReservationDTO {

    private Long customerId;
    private String startDateTime;
    private String endDateTime;
    private Integer numberOfPeople;

    protected ReservationDTO(){

    }
    public ReservationDTO(
            Long customerId,
            String startDateTime,
            String endDateTime,
            Integer numberOfPeople
    ) {
        this.customerId = customerId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.numberOfPeople = numberOfPeople;
    }

    private ReservationDTO(Reservation reservation) {
        this(
                reservation.getCustomer().getId(),
                reservation.getInterval().getStartDateTime().toString(),
                reservation.getInterval().getEndDateTime().toString(),
                reservation.getNumberOfPeople()
        );
    }
    @NonNull
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    @NonNull
    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    @NonNull
    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    @NonNull
    public Integer getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(Integer numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public static ReservationDTO from(Reservation reservation) {
        return new ReservationDTO(reservation);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ReservationDTO that)) return false;
        return Objects.equals(customerId, that.customerId)
                && Objects.equals(startDateTime, that.startDateTime)
                && Objects.equals(endDateTime, that.endDateTime)
                && Objects.equals(numberOfPeople, that.numberOfPeople);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, startDateTime, endDateTime, numberOfPeople);
    }
}
