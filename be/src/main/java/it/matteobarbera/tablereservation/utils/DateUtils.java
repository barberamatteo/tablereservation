package it.matteobarbera.tablereservation.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

}
