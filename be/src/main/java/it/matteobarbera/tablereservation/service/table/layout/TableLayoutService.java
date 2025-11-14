package it.matteobarbera.tablereservation.service.table.layout;

import it.matteobarbera.tablereservation.http.LayoutAPIError;
import it.matteobarbera.tablereservation.http.LayoutAPIInfo;
import it.matteobarbera.tablereservation.http.LayoutAPIResult;
import it.matteobarbera.tablereservation.http.request.IncompleteIdsMapException;
import it.matteobarbera.tablereservation.model.table.AbstractTable;
import it.matteobarbera.tablereservation.model.table.layout.Joinable;
import it.matteobarbera.tablereservation.model.table.layout.SimpleMatrixLayout;
import it.matteobarbera.tablereservation.model.table.layout.UnjoinableTableException;
import it.matteobarbera.tablereservation.repository.table.layout.TableLayoutRepository;
import it.matteobarbera.tablereservation.service.table.TablesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class TableLayoutService {


    private static final Logger log = LoggerFactory.getLogger(TableLayoutService.class);
    private final TableLayoutRepository tableLayoutRepository;
    private final TablesService tablesService;

    public TableLayoutService(TableLayoutRepository tableLayoutRepository, TablesService tablesService) {
        this.tableLayoutRepository = tableLayoutRepository;
        this.tablesService = tablesService;
    }

    public void saveLayout(SimpleMatrixLayout layout) {
        tableLayoutRepository.save(layout);
    }

    public LayoutAPIResult getLayoutById(Long id) {
        var res = tableLayoutRepository.findById(id);
        if (res.isPresent()) {
            return new LayoutAPIResult.Success(res, LayoutAPIInfo.LAYOUT_FETCHED_OK);
        } else {
            return new LayoutAPIResult.Failure(LayoutAPIError.NO_SUCH_LAYOUT_WITH_ID);
        }
    }

    public LayoutAPIResult getLayoutByName(String name) {
        var res = tableLayoutRepository.findByName(name);
        if (res != null){
            return new LayoutAPIResult.Success(res, LayoutAPIInfo.LAYOUT_FETCHED_OK);
        } else {
            return new LayoutAPIResult.Failure(LayoutAPIError.NO_SUCH_LAYOUT_WITH_NAME);
        }
    }

    public LayoutAPIResult createLayout(String name, Map<Long, Set<Long>> idsMap) {
        if (getLayoutByName(name) instanceof LayoutAPIResult.Success){
            return new LayoutAPIResult.Failure(LayoutAPIError.LAYOUT_WITH_NAME_ALREADY_EXISTS);
        }

        var adjacencyMap = resolveAdjacencyMap(idsMap);
        if (adjacencyMap != null && adjacencyMap.isEmpty()) {
            return new LayoutAPIResult.Failure(LayoutAPIError.NO_TABLES_FOUND);
        }
        if (adjacencyMap != null) {
            SimpleMatrixLayout layout = new SimpleMatrixLayout(name, adjacencyMap.keySet());
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


    private Map<AbstractTable, Set<AbstractTable>> resolveAdjacencyMap(Map<Long, Set<Long>> idsMap){
        if (detectMissingTableIds(idsMap)){
            throw new IncompleteIdsMapException(idsMap, IncompleteIdsMapException.Cause.INCOMPLETE_IDS_MAP);
        }
        Map<AbstractTable, Set<AbstractTable>> adjacencyMap = new HashMap<>();
        for (var entry : idsMap.entrySet()) {
            var key = tablesService.getTableById(entry.getKey());
            if (key.isEmpty())
                return null;
            if (detectUnjoinableTable(key.get())){
                throw new UnjoinableTableException(key.get(), UnjoinableTableException.Cause.UNJOINABLE_TABLE_FOUND);
            }
            var adjacentTables = tablesService.getAllTablesById(entry.getValue());
            if (adjacentTables.size() != entry.getValue().size())
                return null;
            if (detectJoinableTables(adjacentTables)){
                throw new UnjoinableTableException(adjacentTables, UnjoinableTableException.Cause.UNJOINABLE_TABLE_FOUND);
            }

            adjacencyMap.put(key.get(), adjacentTables);
        }
        return adjacencyMap;
    }


    /**
     * @return true if the argument isn't joinable, false otherwise
     */
    private boolean detectUnjoinableTable(AbstractTable abstractTable) {
         return !(abstractTable instanceof Joinable);
    }


    /**
     *
     * @return true if exists one (or more) abstractTable in abstractTables which is not Joinable
     */
    private boolean detectJoinableTables(Collection<AbstractTable> abstractTables) {
        return abstractTables.stream().anyMatch(this::detectUnjoinableTable);
    }

    /**
     * @return true if a table found in a Set of the valueSet is not contained in keySet
     */
    private boolean detectMissingTableIds(Map<Long, Set<Long>> adjacencyMap){
        var keySet = adjacencyMap.keySet();
        var valueSet = adjacencyMap.values();

        for (var value : valueSet){
            if (value.stream().anyMatch(table -> !keySet.contains(table)))
                return true;
        }

        return false;
    }

}
