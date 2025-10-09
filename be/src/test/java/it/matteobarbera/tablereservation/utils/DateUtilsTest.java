package it.matteobarbera.tablereservation.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateUtilsTest {

    @Test
    public void offsetFromTest(){
        Long offset = 90L;
        String arrivalDateTime = "2025-10-10T00:00:00";
        assertEquals(
                "2025-10-10T01:30:00",
                DateUtils.offsetFrom(offset, arrivalDateTime)
        );


    }
}
