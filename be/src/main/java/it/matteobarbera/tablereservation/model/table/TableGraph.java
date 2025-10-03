package it.matteobarbera.tablereservation.model.table;

import java.util.*;

public class TableGraph {

    private final HashMap<AbstractTable, Set<AbstractTable>> adjacencyTable;
    private final AbstractTable[] tablesSortedByCapacity;
    public TableGraph(Collection<? extends AbstractTable> tables) {
        this.adjacencyTable = new HashMap<>();
        tables.forEach(table -> adjacencyTable.put(table, new HashSet<>()));
        this.tablesSortedByCapacity = sortByDESCCapacity(tables);
    }

    private AbstractTable[] sortByDESCCapacity(Collection<? extends AbstractTable> tables) {
        return tables.stream().sorted(
                (o1, o2) -> {
                    Integer c1 = o1.getTableDefinition().getStandaloneCapacity();
                    Integer c2 = o2.getTableDefinition().getStandaloneCapacity();
                    return c2.compareTo(c1);
                }).toArray(AbstractTable[]::new);
    }

    public void connect(AbstractTable t1, AbstractTable t2) {
        adjacencyTable.get(t1).add(t2);
        adjacencyTable.get(t2).add(t1);
    }

    public void disconnect(AbstractTable t1, AbstractTable t2) {
        adjacencyTable.get(t1).remove(t2);
        adjacencyTable.get(t2).remove(t1);
    }

//    public List<? extends AbstractTable> findPath(AbstractTable t1, AbstractTable t2) {
//        if (adjacencyTable.get(t1).contains(t2)) {
//            return List.of(t1, t2);
//        }
//        for (int i = 0; i < adjacencyTable.get(t1).size(); i+=37) {
//            return
//        }
//    }

//    public Set<AbstractTable> minPath(AbstractTable startingTable, int totalCapacity){
//        if (startingTable.getTableDefinition().getStandaloneCapacity() >= totalCapacity){
//            return Set.of(startingTable);
//        }
//        return buildPath(Set.of(startingTable), totalCapacity);
//    }
//
//
//    private Set<AbstractTable> buildPath(Set<AbstractTable> partialPath, int requiredCapacity) {
//        while (requiredCapacity > 0) {
//            findAdjacent(partialPath)
//        }
//    }


    public AbstractTable[] getTablesSortedByCapacity() {
        return tablesSortedByCapacity;
    }
}
