package it.matteobarbera.tablereservation.model.table.layout;

import it.matteobarbera.tablereservation.model.table.AbstractTable;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table
final class TableEdge {
    @Id
    @SequenceGenerator(
            name = "edge_sequence",
            sequenceName = "edge_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "edge_sequence"
    )
    private Long id;

    @ManyToOne
    private TableGraph graph;

    @ManyToOne
    @JoinColumn(name = "t_1_id")
    private AbstractTable t1;

    @ManyToOne
    @JoinColumn(name = "t_2_id")
    private AbstractTable t2;


    void setId(Long id) {
        this.id = id;
    }

    Long getId() {
        return id;
    }

    public TableEdge() {
    }

    TableEdge(TableGraph graph, AbstractTable t1, AbstractTable t2) {
        this.graph = graph;
        this.t1 = t1;
        this.t2 = t2;
    }

    AbstractTable getT1() {
        return t1;
    }

    void setT1(AbstractTable t1) {
        this.t1 = t1;
    }

    AbstractTable getT2() {
        return t2;
    }

    void setT2(AbstractTable t2) {
        this.t2 = t2;
    }

    TableGraph getGraph() {
        return graph;
    }

    void setGraph(TableGraph graph) {
        this.graph = graph;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TableEdge tableEdge)) return false;
        return Objects.equals(graph, tableEdge.graph) && Objects.equals(t1, tableEdge.t1) && Objects.equals(t2, tableEdge.t2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(graph, t1, t2);
    }
}
