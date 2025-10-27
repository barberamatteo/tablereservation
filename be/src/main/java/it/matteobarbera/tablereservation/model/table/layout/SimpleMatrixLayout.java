package it.matteobarbera.tablereservation.model.table.layout;

import it.matteobarbera.tablereservation.model.table.AbstractTable;
import jakarta.persistence.*;

import java.util.Collection;

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


    public SimpleMatrixLayout(String name, Collection<AbstractTable> tables) {
        this.name = name;
        this.tableGraph = new TableGraph(tables);
    }

    public boolean connect(AbstractTable t1, AbstractTable t2) {
        return tableGraph.connect(t1, t2);
    }

    public boolean disconnect(AbstractTable t1, AbstractTable t2) {
        return tableGraph.disconnect(t1, t2);
    }


    public TableGraph getGraph() {
        return tableGraph;
    }



}
