package it.matteobarbera.tablereservation.model.table.layout;

import it.matteobarbera.tablereservation.model.table.AbstractTable;
import it.matteobarbera.tablereservation.model.table.SimpleJoinableTable;
import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Entity
public class TableGraph {


    private static final Logger log = LoggerFactory.getLogger(TableGraph.class);
    @Id
    @SequenceGenerator(
            name = "graph_sequence",
            sequenceName = "graph_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "graph_sequence"
    )
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "graph_id")
    private Set<AbstractTable> tables = new HashSet<>();

    @OneToMany(mappedBy = "graph", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<TableEdge> edges = new HashSet<>();

    @Transient
    private HashMap<AbstractTable, Set<AbstractTable>> adjacencyTable;

    public TableGraph(Collection<AbstractTable> tables) {
        initAdjacencyMap(tables);

    }

    public TableGraph() {
    }


    private void initAdjacencyMap(Collection<AbstractTable> tables) {
        log.info("Running initAdjacencyMap");
        this.adjacencyTable = new HashMap<>();
        tables.forEach(table -> adjacencyTable.put(table, new HashSet<>()));
        log.info("Finish initAdjacencyMap, size = {}", adjacencyTable.size());
    }

    public boolean containsTable(AbstractTable table) {
        return adjacencyTable.containsKey(table);
    }
    public boolean connect(AbstractTable t1, AbstractTable t2) {
        TableEdge e = new TableEdge(this, t1, t2);
        boolean alreadyPresent = edges.add(e);
        return adjacencyTable.get(t1).add(t2) && adjacencyTable.get(t2).add(t1) && alreadyPresent;
    }

    public boolean disconnect(AbstractTable t1, AbstractTable t2) {
        TableEdge corresponding1 = new TableEdge(this, t1, t2);
        TableEdge corresponding2 = new TableEdge(this, t2, t1);
        boolean sanityCheck = edges.remove(corresponding1) && edges.remove(corresponding2);
        return adjacencyTable.get(t1).remove(t2) && adjacencyTable.get(t2).remove(t1) && sanityCheck;
    }

    public Set<List<SimpleJoinableTable>> getAllPaths(Set<SimpleJoinableTable> tables, List<Integer> capacities){
        Set<List<SimpleJoinableTable>> paths = new HashSet<>();
        for (SimpleJoinableTable table : tables) {
            paths.addAll(getAllPathsByStartingTable(table, capacities));
        }
        return paths;
    }

    public Set<List<SimpleJoinableTable>> getAllPathsByStartingTable(
            SimpleJoinableTable start,
            List<Integer> capacities
    ){
        Set<List<SimpleJoinableTable>> paths = new HashSet<>();
        List<SimpleJoinableTable> path = new ArrayList<>();
        path.add(start);
        List<Integer> pathCapacity = new ArrayList<>(capacities);
        pathCapacity.remove(Integer.valueOf(start.getStandaloneCapacity()));

        buildPath(paths, path, start, pathCapacity);
        return paths;
    }

    private void buildPath(
            Set<List<SimpleJoinableTable>> paths,
            List<SimpleJoinableTable> path,
            AbstractTable start,
            List<Integer> pathCapacity
    ) {
        if (pathCapacity.isEmpty()) {
            paths.add(new ArrayList<>(path));
            return;
        }

        for (AbstractTable currTable : adjacencyTable.get(start)) {
            if (currTable instanceof SimpleJoinableTable currSimpleJoinableTable) {
                if (path.contains(currSimpleJoinableTable))
                    continue;
                if (!pathCapacity.remove(Integer.valueOf(currSimpleJoinableTable.getStandaloneCapacity())))
                    continue;
                path.add(currSimpleJoinableTable);
                buildPath(paths, path, currTable, pathCapacity);
                pathCapacity.add(currSimpleJoinableTable.getStandaloneCapacity());
                path.removeLast();
            }
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<AbstractTable> getTables() {
        return tables;
    }

    public Set<TableEdge> getEdges() {
        return edges;
    }

}
