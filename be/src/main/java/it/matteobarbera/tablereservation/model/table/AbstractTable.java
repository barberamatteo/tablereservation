package it.matteobarbera.tablereservation.model.table;


import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity(name = "tables")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "table_type")
public abstract class AbstractTable implements Serializable {

    @Id
    @SequenceGenerator(
            name = "table_sequence",
            sequenceName = "table_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "table_sequence"
    )
    protected Long id;

    @Column(name = "number")
    protected int numberInLounge;

    @ManyToOne
    @JoinColumn(name = "table_definition_category")
    protected TableDefinition tableDefinition;


    public AbstractTable(int numberInLounge, TableDefinition tableDefinition) {
        this.numberInLounge = numberInLounge;
        this.tableDefinition = tableDefinition;
    }

    public AbstractTable() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumberInLounge() {
        return numberInLounge;
    }

    public void setNumberInLounge(int numberInLounge) {
        this.numberInLounge = numberInLounge;
    }

    public TableDefinition getTableDefinition() {
        return tableDefinition;
    }

    public void setTableDefinition(TableDefinition tableDefinition) {
        this.tableDefinition = tableDefinition;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + id +
                ", \"numberInLounge\":" + numberInLounge +
                ", \"tableDefinition\":" + tableDefinition.toString() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AbstractTable that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
