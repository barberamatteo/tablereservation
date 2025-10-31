package it.matteobarbera.tablereservation.service.table;

import it.matteobarbera.tablereservation.model.table.*;
import it.matteobarbera.tablereservation.repository.table.TablesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
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

    public Optional<AbstractTable> getTableByNum(Integer num) {
        return tablesRepository.findByNumberInLounge(num);
    }

    public Optional<AbstractTable> getTableById(Long id) {
        return tablesRepository.findById(id);
    }

    public Set<AbstractTable> getAllTablesById(Set<Long> ids) {
        return new HashSet<>(tablesRepository.findAllById(ids));
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

    public void createSimpleTable(SimpleTable simpleTable) {
        tablesRepository.save(simpleTable);
    }

    public void createSimpleJoinableTable(SimpleJoinableTable simpleJoinableTable) {
        tablesRepository.save(simpleJoinableTable);
    }

    public Set<AbstractTable> getAllTables() {
        return Set.copyOf(tablesRepository.findAll());
    }

//
//    public Set<CustomTable> getAllIntervalCompliantTables(Interval interval) {
//
//    }





}
