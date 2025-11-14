package it.matteobarbera.tablereservation.model.table.layout;

import it.matteobarbera.tablereservation.model.table.AbstractTable;
import it.matteobarbera.tablereservation.model.table.SimpleJoinableTable;
import jakarta.persistence.*;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "table_layout")
public class SimpleMatrixLayout {
    @Id
    @SequenceGenerator(
            name = "layout_sequence",
            sequenceName = "layout_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "layout_sequence"
    )
    private Long id;

    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    private TableGraph tableGraph;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    protected SimpleMatrixLayout() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SimpleMatrixLayout(String name, Collection<AbstractTable> tables) {
        this.name = name;
        this.tableGraph = new TableGraph(tables);
    }

    public void connect(AbstractTable t1, AbstractTable t2) {
        tableGraph.connect(t1, t2);
    }

    public void connectAll(AbstractTable t1, Set<AbstractTable> tables) {
        for (AbstractTable t2 : tables) {
            connect(t1, t2);
        }
    }

    public boolean disconnect(AbstractTable t1, AbstractTable t2) {
        return tableGraph.disconnect(t1, t2);
    }


    // TODO: Align table type (remove List<AbstractTable> ---> List<SimpleJoinableTable> conversion)
    public List<SimpleJoinableTable> findBestPathWithExclusionsAndCapacities(
            Set<AbstractTable> whitelistedTables,
            List<Integer> capacities
    ){
        var allPaths = tableGraph.getAllPaths(
                whitelistedTables.stream().map(abstractTable -> new SimpleJoinableTable(abstractTable.getNumberInLounge(), abstractTable.getTableDefinition())).collect(Collectors.toSet()),
                capacities
        );

        PathOptimizationPipeline pipeline = new PathOptimizationPipeline(allPaths);
        var aPath = pipeline.getShortest().getRandom();
        return aPath;

    }

    public TableGraph getGraph() {
        return tableGraph;
    }



    private static final class PathOptimizationPipeline {
        private Set<List<SimpleJoinableTable>> paths;
        public PathOptimizationPipeline(Set<List<SimpleJoinableTable>> paths) {
            this.paths = paths;
        }

        public PathOptimizationPipeline getShortest(){
            TreeSet<List<SimpleJoinableTable>> pathsOrderedBySize = new TreeSet<>(Comparator.comparingInt(List::size));
            pathsOrderedBySize.addAll(paths);
            int minLength = pathsOrderedBySize.first().size();

            paths = pathsOrderedBySize.stream().takeWhile(simpleJoinableTables ->
                    simpleJoinableTables.size() == minLength
            ).collect(Collectors.toSet());
            return this;
        }


        public List<SimpleJoinableTable> getRandom(){
            return paths.iterator().next();
        }

    }
}
