package it.matteobarbera.tablereservation.service.table;

import it.matteobarbera.tablereservation.model.table.SimpleTable;
import it.matteobarbera.tablereservation.model.table.TableCRUDException;
import it.matteobarbera.tablereservation.model.table.TableDefinition;
import it.matteobarbera.tablereservation.repository.table.TablesRepository;
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

    public Optional<SimpleTable> getTableByNum(Integer num) {
        return tablesRepository.findByNumberInLounge(num);
    }

    public void createTable(String category, Integer number) {
        Optional<TableDefinition> def = tablesDefinitionService.getDefByCategory(category);
        def.ifPresentOrElse(
                tableDefinition -> tablesRepository.save(
                        new SimpleTable(
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


    public Set<SimpleTable> getAllTables() {
        return Set.copyOf(tablesRepository.findAll());
    }
//
//    public Set<CustomTable> getAllIntervalCompliantTables(Interval interval) {
//
//    }





}
