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

    public boolean connect(TableType t1, TableType t2) {
        return adjacencyTable.get(t1).add(t2) && adjacencyTable.get(t2).add(t1);
    }

    public boolean disconnect(TableType t1, TableType t2) {
        return adjacencyTable.get(t1).remove(t2) && adjacencyTable.get(t2).remove(t1);
    }

    public Set<List<TableType>> getAllPaths(Set<TableType> tables, List<Integer> capacities){
        Set<List<TableType>> paths = new HashSet<>();
        for (TableType table : tables) {
            paths.addAll(getAllPathsByStartingTable(table, capacities));
        }
        return paths;
    }


    public Set<List<TableType>> getAllPathsByStartingTable(
            TableType start,
            List<Integer> capacities
    ){
        Set<List<TableType>> paths = new HashSet<>();
        List<TableType> path = new ArrayList<>();
        path.add(start);
        List<Integer> pathCapacity = new ArrayList<>(capacities);
        pathCapacity.remove(Integer.valueOf(start.getStandaloneCapacity()));

        buildPath(paths, path, start, pathCapacity);
        return paths;
    }

    private void buildPath(
            Set<List<TableType>> paths,
            List<TableType> path,
            TableType start,
            List<Integer> pathCapacity
    ) {
        if (pathCapacity.isEmpty()) {
            paths.add(new ArrayList<>(path));
            return;
        }

        for (TableType currTable : adjacencyTable.get(start)) {
            if (path.contains(currTable))
                continue;
            if (!pathCapacity.remove(Integer.valueOf(currTable.getStandaloneCapacity())))
                continue;
            path.add(currTable);
            buildPath(paths, path, currTable, pathCapacity);
            pathCapacity.add(currTable.getStandaloneCapacity());
            path.removeLast();
        }
    }


    public List<TableType> getTablesSortedByCapacity() {
        return tablesSortedByCapacity;
    }
}
