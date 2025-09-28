package it.matteobarbera.tablereservation.model.table;

import org.springframework.stereotype.Component;

import java.util.*;

public class TableGraph {

    private final HashMap<AbstractTable, Set<AbstractTable>> adjacencyTable;
    public TableGraph(Collection<AbstractTable> tables) {
        this.adjacencyTable = new HashMap<>();
        tables.forEach(table -> adjacencyTable.put(table, new HashSet<>()));
    }

    public void connect(AbstractTable t1, AbstractTable t2) {
        adjacencyTable.get(t1).add(t2);
        adjacencyTable.get(t2).add(t1);
    }

    public void disconnect(AbstractTable t1, AbstractTable t2) {
        adjacencyTable.get(t1).remove(t2);
        adjacencyTable.get(t2).remove(t1);
    }

    public Set<AbstractTable> minPath(AbstractTable startingTable, int totalCapacity){
        if (startingTable.getTableDefinition().getStandaloneCapacity() < totalCapacity){
            return Set.of();
        }
        return buildPath(Set.of(startingTable));
    }


//    private Set<AbstractTable> buildPath(Set<AbstractTable> partialPath) {
//
//    }


}
