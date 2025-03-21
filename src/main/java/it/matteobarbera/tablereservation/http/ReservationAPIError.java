package it.matteobarbera.tablereservation.http;

public enum ReservationAPIError {


    GENERAL_ERROR("An unexpected error occurred."),

    RESERVATION_DELETE_ERROR(
            "Error occurred while deleting reservation with ID %d" +
            ": reservation is either invalid in its schedule or already deleted."
    ),

    NO_AVAILABLE_TABLES("No available tables for this reservation"),

    NO_RESERVATION_WITH_ID( "No reservation with ID %d was found"),

    NO_RESERVATION_YET_TODAY("There are no reservations yet today."),

    NEED_TO_RESCHEDULE("New numberOfPeople exceeds the total capacity of table cluster. Need to reschedule"),
    NO_RESERVATION_WITH_ID_IN_SCHEDULE("No reservation with ID %d was found in schedule. Maybe it was deleted?"),;

    private final String template;
    ReservationAPIError(String template) {
        this.template = template;
    }

    public String getMessage(Object... args){
        return String.format(template, args);
    }
}
