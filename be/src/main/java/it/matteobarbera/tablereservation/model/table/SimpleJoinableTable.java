package it.matteobarbera.tablereservation.model.table;

import it.matteobarbera.tablereservation.model.table.layout.Joinable;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("simple_joinable_table")
public class SimpleJoinableTable extends AbstractTable implements Joinable {

    private Integer headCapacity;
    private Integer joiningCapacity;

    public SimpleJoinableTable(int numberInLounge, TableDefinition category) {
        super(numberInLounge, category);
        this.headCapacity = 1;
        this.joiningCapacity = tableDefinition.getStandaloneCapacity() - 2 * headCapacity;
    }

    public SimpleJoinableTable(
            int numberInLounge,
            TableDefinition category,
            int headCapacity,
            int joiningCapacity
    ) {
        super(numberInLounge, category);
        this.headCapacity = headCapacity;
        this.joiningCapacity = joiningCapacity;
    }


    protected SimpleJoinableTable() {

    }


    @Override
    public int getStandaloneCapacity() {
        return tableDefinition.getStandaloneCapacity();
    }

    @Override
    public int getJoiningCapacity() {
        return joiningCapacity;
    }

    @Override
    public int getHeadCapacity() {
        return headCapacity;
    }
}
