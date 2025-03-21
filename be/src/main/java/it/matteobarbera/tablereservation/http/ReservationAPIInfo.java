package it.matteobarbera.tablereservation.http;

public enum ReservationAPIInfo {

    RESERVATION_CREATED_OK(null),

    RESERVATION_FETCHED_OK(null),

    RESERVATION_DELETED_OK("Reservation deleted successfully"),

    RESERVATION_UPDATE_OK("Reservation with ID %d" +
            " has been updated successfully with a new number of people of " +
            "%d."
    );

    private final String template;
    ReservationAPIInfo(String template) {
        this.template = template;
    }

    public String getMessage(Object... args){
        return String.format(template, args);
    }


}
