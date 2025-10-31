package it.matteobarbera.tablereservation.model.table.layout;

import it.matteobarbera.tablereservation.model.table.AbstractTable;
import jakarta.persistence.*;

import java.util.Collection;
import java.util.Set;

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


    public TableGraph getGraph() {
        return tableGraph;
    }



}
