package it.matteobarbera.tablereservation.model;

import it.matteobarbera.tablereservation.model.reservation.Interval;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IntervalTest {

    @Test
    public void clashTest(){
        Interval i1 = new Interval("2025-01-06T12:00:00","2025-01-06T14:00:00");
        Interval i2 = new Interval("2025-01-06T13:00:00","2025-01-06T15:00:00");
        assertTrue(i1.clashes(i2));
    }

    @Test
    public void printTest(){
        System.out.println(new Interval("2025-01-06T12:00:00","2025-01-06T14:00:00"));
        System.out.println(new Interval("2025-01-06T00:12:00","2025-01-06T14:00:00"));
    }

    @Test
    public void printDateTest(){
        Interval i1 = new Interval("2025-01-06T12:00:00","2025-01-06T14:00:00");

        assertEquals("2025-01-06", i1.getStartDateTime().toLocalDate().toString());
    }
}
