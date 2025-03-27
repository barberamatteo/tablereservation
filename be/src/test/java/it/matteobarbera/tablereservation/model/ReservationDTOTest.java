package it.matteobarbera.tablereservation.model;

import it.matteobarbera.tablereservation.model.dto.JSONConstructable;
import it.matteobarbera.tablereservation.model.dto.ReservationDTO;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationDTOTest {

    @Test
    public void jsonConstructor() throws
            NoSuchMethodException,
            IllegalAccessException,
            InvocationTargetException,
            InstantiationException,
            NoSuchFieldException{


        Map<String, Object> frontendDataCorrect = new HashMap<>(){{
           put("customerId", 1L);
           //YYYY-mm-DDTHH:MM:SS
           put("startDateTime", "2025-01-01T00:00:00");
           put("endDateTime", "2025-01-01T02:00:00");
           put("numberOfPeople", 2);
        }};


        Map<String, Object> frontendDataIncorrectField = new HashMap<>(frontendDataCorrect);
        frontendDataIncorrectField.remove("startDateTime");
        //INCORRECT FIELD
        frontendDataIncorrectField.put("startDateTim", "2025-01-01T00:00:00");

        Map<String, Object> frontendDataIncompleteFields = new HashMap<>(frontendDataCorrect);
        frontendDataIncompleteFields.remove("endDateTime");


        ReservationDTO reservationDTOFromCorrectData =
                new ReservationDTO(
                        1L,
                        "2025-01-01T00:00:00",
                        "2025-01-01T02:00:00",
                        2
                );

        assertEquals(
                reservationDTOFromCorrectData,
                JSONConstructable.construct(frontendDataCorrect, ReservationDTO.class)
        );

        assertThrows(
                NoSuchFieldException.class,
                () -> JSONConstructable.construct(frontendDataIncorrectField, ReservationDTO.class)
        );

        assertNotEquals(
                reservationDTOFromCorrectData,
                JSONConstructable.construct(frontendDataIncompleteFields, ReservationDTO.class)
        );





    }
}
