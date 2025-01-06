package it.matteobarbera.tablereservation.model.table;

import it.matteobarbera.tablereservation.model.table.admin.TableDefinition;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tables")
public class CustomTable extends AbstractTable {

    public CustomTable(int numberInLounge, TableDefinition category) {
        super(numberInLounge, category);
    }

    public CustomTable() {

    }



}
