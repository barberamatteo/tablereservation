package it.matteobarbera.tablereservation.model.table.layout;

import it.matteobarbera.tablereservation.model.table.SimpleJoinableTable;

import java.util.*;

public class TableGraph<TableType extends SimpleJoinableTable> {

    private final HashMap<TableType, Set<TableType>> adjacencyTable;
    private final List<TableType> tablesSortedByCapacity;
    public TableGraph(Collection<TableType> tables) {
        this.adjacencyTable = new HashMap<>();
        tables.forEach(table -> adjacencyTable.put(table, new HashSet<>()));
        this.tablesSortedByCapacity = sortByDESCCapacity(tables);
    }

    public TableGraph(HashMap<TableType, Set<TableType>> adjacencyTable) {
        this.adjacencyTable = adjacencyTable;
        this.tablesSortedByCapacity = sortByDESCCapacity(adjacencyTable.keySet());
    }

    private List<TableType> sortByDESCCapacity(Collection<TableType> tables) {
        return tables.stream().sorted(
                (o1, o2) -> {
                    Integer c1 = o1.getStandaloneCapacity();
                    Integer c2 = o2.getStandaloneCapacity();
                    return c2.compareTo(c1);
                }).toList();
    }

    public void connect(TableType t1, TableType t2) {
        adjacencyTable.get(t1).add(t2);
        adjacencyTable.get(t2).add(t1);
    }

    public void disconnect(TableType t1, TableType t2) {
        adjacencyTable.get(t1).remove(t2);
        adjacencyTable.get(t2).remove(t1);
    }

    public Set<List<TableType>> getPathsWithinTablesHavingCapacities(
            Collection<TableType> tables,
            List<Integer> capacities,
            int targetCapacity) {
        Set<List<TableType>> paths = new HashSet<>();
        for (TableType table : tables) {
            List<TableType> path = buildPath(table, capacities, targetCapacity);
            if (!path.isEmpty()) {
                paths.add(path);
            }
        }
        return paths;
    }

    private List<TableType> buildPath(TableType table, List<Integer> capacities, int capacity) {
        int residualCapacity = capacity;
        List<TableType> toRet = new ArrayList<>();
        TableType currentTable = table;
        while (residualCapacity > 0 && !capacities.isEmpty()) {

        }
        return toRet;
    }

    public List<TableType> getTablesSortedByCapacity() {
        return tablesSortedByCapacity;
    }
}
