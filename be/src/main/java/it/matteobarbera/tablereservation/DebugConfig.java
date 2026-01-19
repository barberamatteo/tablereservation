package it.matteobarbera.tablereservation;

import it.matteobarbera.tablereservation.model.customer.Customer;
import it.matteobarbera.tablereservation.model.table.AbstractTable;
import it.matteobarbera.tablereservation.model.table.SimpleJoinableTable;
import it.matteobarbera.tablereservation.model.table.TableDefinition;
import it.matteobarbera.tablereservation.model.table.layout.SimpleMatrixLayout;
import it.matteobarbera.tablereservation.service.customer.CustomerService;
import it.matteobarbera.tablereservation.model.dto.CustomerDTO;
import it.matteobarbera.tablereservation.model.dto.ReservationDTO;
import it.matteobarbera.tablereservation.service.table.TablesDefinitionService;
import it.matteobarbera.tablereservation.service.table.TablesService;
import it.matteobarbera.tablereservation.orchestrator.ReservationHandlingOrchestrator;
import it.matteobarbera.tablereservation.service.security.SecurityService;
import it.matteobarbera.tablereservation.service.table.layout.TableLayoutService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

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
            ReservationHandlingOrchestrator reservationHandlingOrchestrator,
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

            TableDefinition piccoloDef = tablesDefinitionService.getDefByCategory("Tavolo piccolo").get();
            TableDefinition grandeDef = tablesDefinitionService.getDefByCategory("Tavolo grande").get();

            SimpleJoinableTable t1 = new SimpleJoinableTable(1, grandeDef);
            SimpleJoinableTable t2 = new SimpleJoinableTable(2, grandeDef);
            SimpleJoinableTable t3 = new SimpleJoinableTable(3, grandeDef);

            SimpleJoinableTable t4 = new SimpleJoinableTable(4, piccoloDef);
            SimpleJoinableTable t5 = new SimpleJoinableTable(5, piccoloDef);
            SimpleJoinableTable t6 = new SimpleJoinableTable(6, piccoloDef);

            tablesService.saveSimpleJoinableTable(t1);
            tablesService.saveSimpleJoinableTable(t2);
            tablesService.saveSimpleJoinableTable(t3);
            tablesService.saveSimpleJoinableTable(t4);
            tablesService.saveSimpleJoinableTable(t5);
            tablesService.saveSimpleJoinableTable(t6);

            Set<AbstractTable> joinableTables = Set.of(t1, t2, t3, t4, t5, t6);
            SimpleMatrixLayout layout = new SimpleMatrixLayout("Layout1", joinableTables);

            layout.connect(t1, t2);
            layout.connect(t1, t3);
            layout.connect(t2, t4);
            tableLayoutService.saveLayout(layout);

            securityService.createAdmin("admin", "admin"); //FE admin for debugging/testing
            securityService.createAdmin("admin_pm", "admin"); //Postman admin for debugging/testing


            SimpleMatrixLayout fetchedLayout = (SimpleMatrixLayout) tableLayoutService.getLayoutById(1L).getSuccess().getResult();


            var res = reservationHandlingOrchestrator.newReservation(
                    new ReservationDTO(
                            customer.getId(),
                            "2030-01-01T01:00:00",
                            "2030-01-01T02:00:00",
                            10
                    ),
                    fetchedLayout.getId()
            );
            log.warn(res.getStatus().toString());
            /*reservationHandlingOrchestrator.newReservation(
                    new ReservationDTO(
                            customer.getId(),
                            "2030-01-01T00:00:00",
                            "2030-01-01T02:00:00",
                            4
                    ),
                    fetchedLayout.getId()
            );
            reservationHandlingOrchestrator.newReservation(
                    new ReservationDTO(
                            customer.getId(),
                            "2030-01-01T00:00:00",
                            "2030-01-01T02:00:00",
                            6
                    ),
                    fetchedLayout.getId()
            );*/
        };
    }
}
