package it.matteobarbera.tablereservation.model;

import it.matteobarbera.tablereservation.model.table.AbstractTable;
import it.matteobarbera.tablereservation.model.table.SimpleJoinableTable;
import it.matteobarbera.tablereservation.model.table.SimpleTable;
import it.matteobarbera.tablereservation.model.table.TableDefinition;
import it.matteobarbera.tablereservation.model.table.layout.TableEdge;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TableEdgeTest {

    @Test
    public void equalityTest() {
        TableDefinition common =  new TableDefinition("cat", 4);
        AbstractTable t1 = new SimpleJoinableTable(1, 1, 2);
        t1.setTableDefinition(common);
        AbstractTable t2 = new SimpleJoinableTable(1, 1, 2);
        t2.setTableDefinition(common);

        TableEdge e1 = new TableEdge();
        e1.setT1(t1);
        e1.setT2(t2);

        TableEdge e2 = new TableEdge();
        e2.setT1(t2);
        e2.setT2(t1);

        assertEquals(e1, e2);
        Set<TableEdge> set = new HashSet<>();
        set.add(e1);
        set.add(e2);
        assertEquals(1, set.size());

    }
}
