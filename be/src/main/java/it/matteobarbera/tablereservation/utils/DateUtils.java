package it.matteobarbera.tablereservation.utils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;

public class DateUtils {

    private DateUtils(){

    }
    public static String estrapolateDate(String dateTime){
        if (dateTime.isEmpty())
            return "";
        String year = dateTime.substring(0, 4);
        String month = dateTime.substring(5, 7);
        String day = dateTime.substring(8, 10);
        return year + "-" + month + "-" + day;
    }

    public static String estrapolateDate(LocalDate date){
        return date.toString();
    }


    public static String offsetFrom(Long defaultLeaveTimeMinutesOffset, String arrivalDateTime) {
        LocalDateTime parsedArrivalDateTime = LocalDateTime.parse(arrivalDateTime);
        return parsedArrivalDateTime
                .plusMinutes(defaultLeaveTimeMinutesOffset)
                .toString();
    }

    public static LocalDate tomorrow(LocalDate localDate) {
        return localDate.plusDays(1);
    }

    public static LocalDateTime atMidnight(LocalDate date) {
        return LocalDateTime.of(date, LocalTime.MIDNIGHT);
    }

    public static LocalDateTime atMidnightMinusOne(LocalDate endDate) {
        return atMidnight(endDate).minusMinutes(1);
    }
}
