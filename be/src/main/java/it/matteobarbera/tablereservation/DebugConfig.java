package it.matteobarbera.tablereservation;

import it.matteobarbera.tablereservation.model.customer.Customer;
import it.matteobarbera.tablereservation.model.table.AbstractTable;
import it.matteobarbera.tablereservation.model.table.layout.SimpleMatrixLayout;
import it.matteobarbera.tablereservation.service.customer.CustomerService;
import it.matteobarbera.tablereservation.model.dto.CustomerDTO;
import it.matteobarbera.tablereservation.model.dto.ReservationDTO;
import it.matteobarbera.tablereservation.service.table.TablesDefinitionService;
import it.matteobarbera.tablereservation.service.table.TablesService;
import it.matteobarbera.tablereservation.facade.ReservationHandlingFacade;
import it.matteobarbera.tablereservation.service.security.SecurityService;
import it.matteobarbera.tablereservation.service.table.layout.TableLayoutService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.Objects;

@Configuration
@ConditionalOnProperty(name = "debug", havingValue = "true")
public class DebugConfig {

    private static final Logger log = LoggerFactory.getLogger(DebugConfig.class);

    @Bean
    CommandLineRunner commandLineRunner(
            TablesDefinitionService tablesDefinitionService,
            TablesService tablesService,
            CustomerService customerService,
            SecurityService securityService,
            ReservationHandlingFacade reservationHandlingFacade,
            ModelMapper modelMapper, TableLayoutService tableLayoutService) {
        return args -> {
            CustomerDTO customerDTO = new CustomerDTO(
                    "Matteo",
                    "3333333333",
                    "email@example.com"
            );
            Customer customer = modelMapper.map(customerDTO, Customer.class);
            customerService.createCustomer(customer);

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


//    @Bean
//    @DependsOn("commandLineRunner")
//    CommandLineRunner commandLineRunner2(TableLayoutService tableLayoutService){
//        return args -> {
//
//            var layout = tableLayoutService.getLayout(1L);
//            log.info("Layout fetched");
//
//            var graph = layout.getGraph();
//            var tables = graph.getTables();
//            var edges =  graph.getEdges();
//            System.out.println(tables);
//            System.out.println(edges);
//        };
//
//    }
}
