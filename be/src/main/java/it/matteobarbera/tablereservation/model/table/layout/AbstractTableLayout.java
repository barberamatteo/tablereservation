package it.matteobarbera.tablereservation.model.table.layout;

import it.matteobarbera.tablereservation.model.table.AbstractTable;
import it.matteobarbera.tablereservation.model.table.TableGraph;

import java.util.Set;

public abstract class AbstractTableLayout {
    private final TableGraph tableGraph;

    public AbstractTableLayout(Set<AbstractTable> tables) {
        this.tableGraph = new TableGraph(tables);
    }

}
