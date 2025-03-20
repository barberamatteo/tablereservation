package it.matteobarbera.tablereservation.http;

public enum ReservationAPIInfo {
    RESERVATION_DELETED_OK{
        @Override
        public String toString() {
            return "Reservation deleted successfully";
        }
    },

    RESERVATION_UPDATE_OK{
        public String toString(Long reservationId, String numberOfPeople){
            return "Reservation with ID " + reservationId +
                    " has been updated successfully with a new number of people of " +
                    numberOfPeople + ".";
        }
    }


}
