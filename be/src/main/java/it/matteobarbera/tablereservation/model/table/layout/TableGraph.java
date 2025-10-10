package it.matteobarbera.tablereservation.model.table.layout;

import it.matteobarbera.tablereservation.model.table.AbstractTable;

import java.util.*;

public class TableGraph<TableType extends AbstractTable> {

    private final HashMap<TableType, Set<TableType>> adjacencyTable;
    private final List<TableType> tablesSortedByCapacity;
    public TableGraph(Collection<TableType> tables) {
        this.adjacencyTable = new HashMap<>();
        tables.forEach(table -> adjacencyTable.put(table, new HashSet<>()));
        this.tablesSortedByCapacity = sortByDESCCapacity(tables);
    }

    private List<TableType> sortByDESCCapacity(Collection<TableType> tables) {
        return tables.stream().sorted(
                (o1, o2) -> {
                    Integer c1 = o1.getTableDefinition().getStandaloneCapacity();
                    Integer c2 = o2.getTableDefinition().getStandaloneCapacity();
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

//    public List<TableType> minGreedyPathWithExclusions(Collection<TableType> tables){
//        tables.forEach(table ->);
//    }

    public List<TableType> getTablesSortedByCapacity() {
        return tablesSortedByCapacity;
    }
}
