package it.matteobarbera.tablereservation.model.dto;

public class ReservationDTO {
    private Long customerId;
    private String startDateTime;
    private String endDateTime;
    private Integer numberOfPeople;

    public ReservationDTO(Long customerId, String startDateTime, String endDateTime, Integer numberOfPeople) {
        this.customerId = customerId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.numberOfPeople = numberOfPeople;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public Integer getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(Integer numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }
}
