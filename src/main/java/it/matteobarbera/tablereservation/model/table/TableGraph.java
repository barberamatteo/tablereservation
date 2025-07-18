package it.matteobarbera.tablereservation.model.table;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TableGraph {
    private static TableGraph INSTANCE;

    private Map<AbstractTable, Set<AbstractTable>> adjacencyMap;

    private TableGraph() {
        this.adjacencyMap = new HashMap<>();
    }

    public static synchronized TableGraph getInstance() {
        if (INSTANCE == null){
            INSTANCE = new TableGraph();
        }
        return INSTANCE;
    }
    public boolean add(AbstractTable node) {
        if (adjacencyMap.containsKey(node))
            return false;
        else{
            adjacencyMap.put(node, new HashSet<>(0));
            return true;
        }
    }

    public boolean connect(AbstractTable node1, AbstractTable node2) {
        if (!adjacencyMap.containsKey(node1) || !adjacencyMap.containsKey(node2))
            return false;
        adjacencyMap.get(node1).add(node2);
        adjacencyMap.get(node2).add(node1);
        return true;
    }

    public Set<AbstractTable> getAdjacentNodes(AbstractTable node) {
        return adjacencyMap.get(node);
    }

    public boolean isConnected(AbstractTable node1, AbstractTable node2) {
        return adjacencyMap.get(node1).contains(node2);
    }





}
