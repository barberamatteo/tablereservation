package it.matteobarbera.tablereservation.model.table;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("simple_table")
public class SimpleTable extends AbstractTable {

    public SimpleTable(int numberInLounge, TableDefinition category) {
        super(numberInLounge, category);
    }

    public SimpleTable() {

    }

}
