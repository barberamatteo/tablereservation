package it.matteobarbera.tablereservation;

import it.matteobarbera.tablereservation.model.table.admin.TablesDefinitionService;
import it.matteobarbera.tablereservation.model.table.admin.TablesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DebugConfig {

    @Bean
    CommandLineRunner commandLineRunner(TablesDefinitionService tablesDefinitionService, TablesService tablesService) {
        return args -> {
            tablesDefinitionService.createNewDef("tavoloda4", 4);
            tablesDefinitionService.createNewDef("tavoloda6", 6);

            tablesService.createTable("tavoloda4", 1);
            tablesService.createTable("tavoloda4", 2);
            tablesService.createTable("tavoloda4", 3);
            tablesService.createTable("tavoloda6", 4);
            tablesService.createTable("tavoloda6", 5);
            tablesService.createTable("tavoloda6", 6);


        };

    }
}
