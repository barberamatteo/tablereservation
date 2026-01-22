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
    public Set<AbstractTable> findBestPathWithExclusionsAndCapacities(
            Set<SimpleJoinableTable> whitelistedTables,
            List<Integer> capacities
    ){
        var allPaths = tableGraph.getAllPaths(
                whitelistedTables,
                capacities
        );

        PathOptimizationPipeline pipeline = new PathOptimizationPipeline(allPaths);
        return pipeline.getShortest().getRandom();

    }

    public TableGraph getGraph() {
        return tableGraph;
    }



    private static final class PathOptimizationPipeline {
        private Set<Set<AbstractTable>> paths;
        public PathOptimizationPipeline(Set<Set<AbstractTable>> paths) {
            this.paths = paths;
        }

        public PathOptimizationPipeline getShortest(){
            TreeSet<Set<AbstractTable>> pathsOrderedBySize = new TreeSet<>(Comparator.comparingInt(Set::size));
            pathsOrderedBySize.addAll(paths);
            int minLength = (pathsOrderedBySize.isEmpty() ? 0: pathsOrderedBySize.first().size());

            paths = pathsOrderedBySize.stream().takeWhile(simpleJoinableTables ->
                    simpleJoinableTables.size() == minLength
            ).collect(Collectors.toSet());
            return this;
        }


        public Set<AbstractTable> getRandom(){
            return (paths.iterator().hasNext() ? paths.iterator().next() : Set.of());
        }

    }
}
