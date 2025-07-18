package it.matteobarbera.tablereservation.model.table.admin;

import it.matteobarbera.tablereservation.model.table.CustomTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class TablesService {
    private final TablesRepository tablesRepository;
    private final TablesDefinitionService tablesDefinitionService;

    @Autowired
    public TablesService(
            TablesRepository tablesRepository,
            TablesDefinitionService tablesDefinitionService
    ) {
        this.tablesRepository = tablesRepository;
        this.tablesDefinitionService = tablesDefinitionService;
    }

    public Optional<CustomTable> getTableByNum(Integer num) {
        return tablesRepository.findByNumberInLounge(num);
    }

    public void createTable(String category, Integer number) {
        Optional<TableDefinition> def = tablesDefinitionService.getDefByCategory(category);
        def.ifPresentOrElse(
                tableDefinition -> tablesRepository.save(
                        new CustomTable(
                                number,
                                tableDefinition
                        )
                ),
                () -> def.orElseThrow(
                        () -> new TableCRUDException(
                                HttpStatus.PRECONDITION_FAILED,
                                category,
                                TableCRUDException.Cause.NO_SUCH_CATEGORY_DEFINED
                        )
                )
        );
    }


    public Set<CustomTable> getAllTables() {
        return Set.copyOf(tablesRepository.findAll());
    }


    public Set<CustomTable> getAdequateTables(Integer numberOfPeople) {
        return tablesRepository.getAdequateTables(numberOfPeople);
    }

}
