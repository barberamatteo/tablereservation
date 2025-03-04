package it.matteobarbera.tablereservation;

import it.matteobarbera.tablereservation.model.table.admin.TablesDefinitionService;
import it.matteobarbera.tablereservation.model.table.admin.TablesService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DebugConfig {

    @Bean
    CommandLineRunner commandLineRunner(TablesDefinitionService tablesDefinitionService, TablesService tablesService) {
        return args -> {
            tablesDefinitionService.createNewDef("Tavolo piccolo", 4);
            tablesDefinitionService.createNewDef("Tavolo grande", 6);

            tablesService.createTable("Tavolo piccolo", 1);
            tablesService.createTable("Tavolo piccolo", 2);
            tablesService.createTable("Tavolo piccolo", 3);
            tablesService.createTable("Tavolo grande", 4);
            tablesService.createTable("Tavolo grande", 5);
            tablesService.createTable("Tavolo grande", 6);


        };

    }
}
