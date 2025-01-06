package it.matteobarbera.tablereservation.model.reservation;

import java.time.LocalDateTime;

public record IntervalIdRecord(
        LocalDateTime startDateTime,
        LocalDateTime endDateTime
) {
}
