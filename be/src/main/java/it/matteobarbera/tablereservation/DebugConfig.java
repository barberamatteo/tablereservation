package it.matteobarbera.tablereservation;

import it.matteobarbera.tablereservation.service.admin.AdminService;
import it.matteobarbera.tablereservation.service.customer.CustomerService;
import it.matteobarbera.tablereservation.model.dto.CustomerDTO;
import it.matteobarbera.tablereservation.model.dto.ReservationDTO;
import it.matteobarbera.tablereservation.service.reservation.ReservationsService;
import it.matteobarbera.tablereservation.service.reservation.ScheduleService;
import it.matteobarbera.tablereservation.service.table.TablesDefinitionService;
import it.matteobarbera.tablereservation.service.table.TablesService;
import it.matteobarbera.tablereservation.controller.ReservationHandlingFacade;
import it.matteobarbera.tablereservation.service.security.SecurityService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DebugConfig {

    @Bean
    CommandLineRunner commandLineRunner(
            TablesDefinitionService tablesDefinitionService,
            TablesService tablesService,
            AdminService adminService, ReservationsService reservationsService, CustomerService customerService, ScheduleService scheduleService, SecurityService securityService, ReservationHandlingFacade reservationHandlingFacade) {
        return args -> {
            customerService.createCustomer(
                    new CustomerDTO(
                            "Matteo",
                            "3333333333",
                            "email@example.com"
                    )
            );

            tablesDefinitionService.createNewDef("Tavolo piccolo", 4);
            tablesDefinitionService.createNewDef("Tavolo grande", 6);

            tablesService.createTable("Tavolo grande", 1);
            tablesService.createTable("Tavolo grande", 2);
            tablesService.createTable("Tavolo grande", 3);
            tablesService.createTable("Tavolo piccolo", 4);
            tablesService.createTable("Tavolo piccolo", 5);
            tablesService.createTable("Tavolo piccolo", 6);

            securityService.createAdmin("admin", "admin"); //FE admin for debugging/testing
            securityService.createAdmin("admin_pm", "admin"); //Postman admin for debugging/testing
            reservationHandlingFacade.newReservation(
                    new ReservationDTO(
                            1L,
                            "2030-01-01T00:00:00",
                            "2030-01-01T02:00:00",
                            4
                    )
            );
            reservationHandlingFacade.newReservation(
                    new ReservationDTO(
                            1L,
                            "2030-01-01T00:00:00",
                            "2030-01-01T02:00:00",
                            6
                    )
            );
        };

    }
}
