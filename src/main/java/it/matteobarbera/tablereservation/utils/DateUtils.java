package it.matteobarbera.tablereservation.utils;

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


}
