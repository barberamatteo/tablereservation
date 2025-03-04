package it.matteobarbera.tablereservation.utils;

public class DateUtils {

    public static String estrapolateDate(String dateTime){
        String year = dateTime.substring(0, 4);
        String month = dateTime.substring(5, 7);
        String day = dateTime.substring(8, 10);
        return year + "-" + month + "-" + day;
    }


}
