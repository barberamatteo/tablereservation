package it.matteobarbera.tablereservation.uc;

import it.matteobarbera.tablereservation.Constants;
import it.matteobarbera.tablereservation.model.table.user.UserReservationApiController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class NewReservationUCTest {

    static final String NEW_RESERVATION_ENDPOINT = Constants.USER_RESERVATION_API_ENDPOINT;

    // Test variables
    @Autowired
    UserReservationApiController userReservationApiController;


    @Autowired
    MockMvc mockMvc;


    // Config

    @Test
    public void newReservationMvcTest() throws Exception {
        mockMvc
                .perform(
                        post(
                                NEW_RESERVATION_ENDPOINT +
                                        "/newreservation/" +
                                        "?customer={customer}" +
                                        "&arrivalDateTime={arrivalDateTime}" +
                                        "&leaveDateTime={time}" +
                                        "&numberOfPeople={numberOfPeople}",

                                1,
                                "2025-01-01T00:00:00",
                                "2025-01-01T00:01:30",
                                6)
                )
                .andExpect(status().isOk());
    }





}
