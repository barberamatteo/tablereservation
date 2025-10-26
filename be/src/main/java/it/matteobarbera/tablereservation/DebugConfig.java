package it.matteobarbera.tablereservation;

import it.matteobarbera.tablereservation.model.customer.Customer;
import it.matteobarbera.tablereservation.model.table.AbstractTable;
import it.matteobarbera.tablereservation.model.table.layout.SimpleMatrixLayout;
import it.matteobarbera.tablereservation.service.admin.AdminService;
import it.matteobarbera.tablereservation.service.customer.CustomerService;
import it.matteobarbera.tablereservation.model.dto.CustomerDTO;
import it.matteobarbera.tablereservation.model.dto.ReservationDTO;
import it.matteobarbera.tablereservation.service.reservation.ReservationsService;
import it.matteobarbera.tablereservation.service.reservation.ScheduleService;
import it.matteobarbera.tablereservation.service.table.TablesDefinitionService;
import it.matteobarbera.tablereservation.service.table.TablesService;
import it.matteobarbera.tablereservation.facade.ReservationHandlingFacade;
import it.matteobarbera.tablereservation.service.security.SecurityService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Configuration
@ConditionalOnProperty(name = "debug", havingValue = "true")
public class DebugConfig {

    @Bean
    CommandLineRunner commandLineRunner(
            TablesDefinitionService tablesDefinitionService,
            TablesService tablesService,
            CustomerService customerService,
            SecurityService securityService,
            ReservationHandlingFacade reservationHandlingFacade,
            ModelMapper modelMapper) {
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

//            TableGraph g = new TableGraph();
//            List<AbstractTable> tables = new ArrayList<>(tablesService.getAllTables());
//            g.connect(tables.get(0), tables.get(1));
//            g.connect(tables.get(1), tables.get(2));
//            tableGraphRepository.save(g);

            var tables = tablesService.getAllTables();
            SimpleMatrixLayout layout = new SimpleMatrixLayout(tables);
            AbstractTable[] a = new AbstractTable[tables.size()];
            for (int i = 0; i < a.length; i++) {
                int finalI = i +1;
                a[i] = tables.stream().filter(abstractTable -> Objects.equals(abstractTable.getId(), (long) finalI)).findFirst().get();
            }
            layout.connect(a[0], a[1]);
            layout.connect(a[1], a[2]);
            layout.connect(a[0], a[3]);
            layout.connect(a[3], a[4]);
            layout.connect(a[1], a[4]);
            layout.connect(a[4], a[5]);
            layout.connect(a[2], a[5]);
        };

    }

//    @Bean
//    CommandLineRunner layout(
//            TablesDefinitionService tablesDefinitionService,
//            TablesService tablesService,
//            CustomerService customerService,
//            SecurityService securityService,
//            ReservationHandlingFacade reservationHandlingFacade,
//            ModelMapper modelMapper
//    ){
//        return args -> {
//            commandLineRunner(
//                    tablesDefinitionService,
//                    tablesService,
//                    customerService,
//                    securityService,
//                    reservationHandlingFacade,
//                    modelMapper
//            );
//
//            var tables = tablesService.getAllTables();
//            SimpleMatrixLayout layout = new SimpleMatrixLayout(tables);
//            layout.connect(tablesService.getTableByNum(1).get(), tablesService.getTableByNum(2).get());
//            layout.connect(tablesService.getTableByNum(2).get(), tablesService.getTableByNum(3).get());
//            layout.connect(tablesService.getTableByNum(1).get(), tablesService.getTableByNum(4).get());
//            layout.connect(tablesService.getTableByNum(2).get(), tablesService.getTableByNum(4).get());
//            layout.connect(tablesService.getTableByNum(3).get(), tablesService.getTableByNum(5).get());
//
//        };
//    }
}
