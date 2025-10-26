package it.matteobarbera.tablereservation.model;

import it.matteobarbera.tablereservation.model.table.SimpleJoinableTable;
import it.matteobarbera.tablereservation.model.table.TableDefinition;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class TableGraphTest {

    @Test
    public void instantiationTest() {
        TableDefinition tableDefinition1 = new TableDefinition("Tavolo grande", 6);
        TableDefinition tableDefinition2 = new TableDefinition("Tavolo piccolo", 4);
        SimpleJoinableTable[] tables = {
                new SimpleJoinableTable(4, tableDefinition2),
                new SimpleJoinableTable(5, tableDefinition2),
                new SimpleJoinableTable(2, tableDefinition1),
                new SimpleJoinableTable(3, tableDefinition1),
                new SimpleJoinableTable(1, tableDefinition1),
                new SimpleJoinableTable(6, tableDefinition2)
        };
        var tableList = Arrays.asList(tables);
        //var tableGraph = new TableGraph<>(tableList);

        //System.out.println(List.toString(tableGraph.getTablesSortedByCapacity()));
    }
}
