package it.matteobarbera.tablereservation.uc;

import it.matteobarbera.tablereservation.Constants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class NewReservationUCTest {

    static final String NEW_RESERVATION_ENDPOINT = Constants.USER_RESERVATION_API_ENDPOINT;
    static final String NEW_DEFINITION_ENDPOINT = Constants.ADMIN_TABLESDEFINITION_CRUD_API_ENDPOINT;
    static final String NEW_TABLE_ENDPOINT = Constants.ADMIN_TABLES_CRUD_API_ENDPOINT;
    static final String ADMIN_ROLE = "ADMIN";

    @Autowired
    MockMvc mockMvc;



    @WithMockUser(roles = ADMIN_ROLE)
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

    /**
     * Creates a test table definition of capacity 99999, create a test table and submits two conflictual
     * reservations of 99999 people.
     * @throws Exception
     */
    @WithMockUser(roles = ADMIN_ROLE)
    @Test
    public void twoConflictualReservationsTest() throws Exception {

        //Defining a test table
        mockMvc.perform(
                post(
                        NEW_DEFINITION_ENDPOINT +
                        "/define/" +
                        "?category={category}" +
                        "&standaloneCapacity={capacity}",

                        "Test Table",
                        99999
                )
        );

        //Creating a test table
        mockMvc.perform(
                post(
                        NEW_TABLE_ENDPOINT +
                        "/create/" +
                        "?category={category}" +
                        "&number={number}",

                        "Test Table",
                        99999
                )
        );

        //Submitting the first reservation
        mockMvc.perform(
                post(
                        NEW_RESERVATION_ENDPOINT +
                        "/newreservation/" +
                        "?customer={customer}" +
                        "&arrivalDateTime={arrivalDateTime}" +
                        "&leaveDateTime={time}" +
                        "&numberOfPeople={numberOfPeople}",

                        1,
                        "2025-01-01T00:00:00",
                        "2025-01-01T02:00:00",
                        99999
                )
        ).andExpect(status().isOk());
        //Submitting the second reservation
        mockMvc.perform(
                post(
                        NEW_RESERVATION_ENDPOINT +
                                "/newreservation/" +
                                "?customer={customer}" +
                                "&arrivalDateTime={arrivalDateTime}" +
                                "&leaveDateTime={time}" +
                                "&numberOfPeople={numberOfPeople}",

                        1,
                        "2025-01-01T01:00:00",
                        "2025-01-01T03:00:00",
                        99999
                )
        ).andExpect(status().isConflict());
    }





}
