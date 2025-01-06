package it.matteobarbera.tablereservation.model.reservation;

import it.matteobarbera.tablereservation.model.table.CustomTable;

public record ScheduleIdRecord(
    CustomTable table,
    String parsedDate
){}

