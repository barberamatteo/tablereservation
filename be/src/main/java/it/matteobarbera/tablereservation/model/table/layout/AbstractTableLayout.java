package it.matteobarbera.tablereservation.model.table.layout;

import it.matteobarbera.tablereservation.model.table.AbstractTable;

import java.util.Set;

public abstract class AbstractTableLayout {
    protected final TableGraph tableGraph;

    public AbstractTableLayout(Set<AbstractTable> tables) {
        this.tableGraph = new TableGraph(tables);
    }



}
