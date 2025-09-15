package it.matteobarbera.tablereservation.service.table;

import it.matteobarbera.tablereservation.model.table.TableDefinition;
import it.matteobarbera.tablereservation.repository.table.TablesDefinitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class TablesDefinitionService {

    private final TablesDefinitionRepository tablesDefinitionRepository;

    @Autowired
    public TablesDefinitionService(TablesDefinitionRepository tablesDefinitionRepository) {
        this.tablesDefinitionRepository = tablesDefinitionRepository;
    }

    public Optional<TableDefinition> getDefByCategory(String category) {
        return tablesDefinitionRepository.getTableDefinitionByCategoryName(category);
    }

    public void createNewDef(String category, Integer standaloneCapacity) {
        tablesDefinitionRepository.save(new TableDefinition(category, standaloneCapacity));
    }

    public Set<TableDefinition> getAllTablesDefinitions() {
        return Set.copyOf(tablesDefinitionRepository.findAll());
    }
}
