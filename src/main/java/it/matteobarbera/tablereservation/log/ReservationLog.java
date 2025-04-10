package it.matteobarbera.tablereservation.log;

public final class ReservationLog {
    private ReservationLog() {}

    public static final String RESERVATION_CREATED = "Reservation {} created";
    public static final String NO_AVAILABLE_TABLE_FOR_RESERVATION = "No available tables for reservation {}";
    public static final String RESERVATION_DELETED = "Reservation with id {} deleted";
    public static final String NO_RESERVATION_TO_DELETE = "Tried to delete non-existing reservation with id {}";
    public static final String ALL_RESERVATIONS_FOUND = "Returning all reservations";
    public static final String NO_RESERVATIONS_FOUND = "Tried to return all reservations (0 found)";
    public static final String RESERVATION_UPDATED = "Reservation with id {} updated";
    public static final String NO_RESERVATION_TO_UPDATE = "Tried to update non-existing reservation with id {}";
    public static final String NO_RESERVATION_TO_UPDATE_IN_SCHEDULE = "Tried to update reservation with id {} not present in any schedule";
    public static final String RESERVATION_TO_RESCHEDULE = "Updating reservation with id {} triggered the creation of the update action token.";
    public static final String RESERVATION_UPDATED_AFTER_TOKEN_CONSUMING = "Reservation updated and token {} consumed";
    public static final String RESERVATION_NOT_UPDATED_AFTER_TOKEN_CONSUMING = "Token {} was consumed, but the reservation associated was not updated!";

    public static final String GENERIC_ERROR = "An unexpected error occurred. See the stack trace.";

}
