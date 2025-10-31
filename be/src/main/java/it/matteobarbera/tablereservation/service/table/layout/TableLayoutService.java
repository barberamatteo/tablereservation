package it.matteobarbera.tablereservation.service.table.layout;

import it.matteobarbera.tablereservation.http.LayoutAPIError;
import it.matteobarbera.tablereservation.http.LayoutAPIInfo;
import it.matteobarbera.tablereservation.http.LayoutAPIResult;
import it.matteobarbera.tablereservation.model.table.AbstractTable;
import it.matteobarbera.tablereservation.model.table.layout.SimpleMatrixLayout;
import it.matteobarbera.tablereservation.repository.table.layout.TableLayoutRepository;
import it.matteobarbera.tablereservation.service.table.TablesService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class TableLayoutService {



    private final TableLayoutRepository tableLayoutRepository;
    private final TablesService tablesService;

    public TableLayoutService(TableLayoutRepository tableLayoutRepository, TablesService tablesService) {
        this.tableLayoutRepository = tableLayoutRepository;
        this.tablesService = tablesService;
    }

    public void saveLayout(SimpleMatrixLayout layout) {
        tableLayoutRepository.save(layout);
    }

    public SimpleMatrixLayout getLayout(Long id) {
        var res = tableLayoutRepository.findById(id);
        return res.orElse(null);
    }

    public LayoutAPIResult createLayout(String name, Map<Long, Set<Long>> idsMap) {
        var adjacencyMap = resolveAdjacencyMap(idsMap);
        if (adjacencyMap != null && adjacencyMap.isEmpty()) {
            return new LayoutAPIResult.Failure(LayoutAPIError.NO_TABLES_FOUND);
        }
        if (adjacencyMap != null) {
            SimpleMatrixLayout layout = new SimpleMatrixLayout(name, adjacencyMap.keySet());
            boolean sanityCheck;
            for (var entry : adjacencyMap.entrySet()) {
                layout.connectAll(entry.getKey(), entry.getValue());
            }
            saveLayout(layout);
            return new LayoutAPIResult.Success(
                    layout,
                    LayoutAPIInfo.LAYOUT_CREATED_OK
            );
        }
        return new LayoutAPIResult.Failure(LayoutAPIError.GENERIC_ERROR);

    }


    private void createEdges(){

    }


    private Map<AbstractTable, Set<AbstractTable>> resolveAdjacencyMap(Map<Long, Set<Long>> idsMap){
        Map<AbstractTable, Set<AbstractTable>> adjacencyMap = new HashMap<>();
        for (var entry : idsMap.entrySet()) {
            var key = tablesService.getTableById(entry.getKey());
            if (key.isEmpty())
                return null;
            var adjacentTables = tablesService.getAllTablesById(entry.getValue());
            if (adjacentTables.size() != entry.getValue().size())
                return null;
            adjacencyMap.put(key.get(), adjacentTables);
        }
        return adjacencyMap;
    }
}
