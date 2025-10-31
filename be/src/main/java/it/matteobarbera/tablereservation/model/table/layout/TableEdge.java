package it.matteobarbera.tablereservation.model.table.layout;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.matteobarbera.tablereservation.model.table.AbstractTable;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table
public class TableEdge {
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
    @JsonIgnore
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

    public AbstractTable getT1() {
        return t1;
    }

    public void setT1(AbstractTable t1) {
        this.t1 = t1;
    }

    public AbstractTable getT2() {
        return t2;
    }

    public void setT2(AbstractTable t2) {
        this.t2 = t2;
    }

    public TableGraph getGraph() {
        return graph;
    }

    public void setGraph(TableGraph graph) {
        this.graph = graph;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TableEdge tableEdge)) {
            return false;
        } else {
            boolean weakEquality =
                    (Objects.equals(t1, tableEdge.t2) && Objects.equals(t2, tableEdge.t1));
            boolean strongEquality =
                    Objects.equals(t1, tableEdge.t1) && Objects.equals(t2, tableEdge.t2);

            return Objects.equals(graph, tableEdge.graph) && (strongEquality || weakEquality);
        }
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
