package it.matteobarbera.tablereservation.http.request;

public class ReservationAPIRequest {
    public final Long customerId;
    public final Integer numberOfPeople;
    public final String startDateTime;
    public String endDateTime;


    public ReservationAPIRequest(Long customerId, Integer numberOfPeople, String startDateTime, String endDateTime) {
        this.customerId = customerId;
        this.numberOfPeople = numberOfPeople;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Integer getNumberOfPeople() {
        return numberOfPeople;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }
}
