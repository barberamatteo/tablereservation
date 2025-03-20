package it.matteobarbera.tablereservation.http;

public enum ReservationAPIError {
    NO_AVAILABLE_TABLES{
        @Override
        public String toString() {
            return "No available tables for this reservation";
        }
    },

    NO_RESERVATION_WITH_ID{
        public String toString(Long id){
            return "No reservation with ID " + id + " was found";
        }
    },

    NO_RESERVATION_YET_TODAY{
        @Override
        public String toString() {
            return "There are no reservations yet today.";
        }
    }

}
