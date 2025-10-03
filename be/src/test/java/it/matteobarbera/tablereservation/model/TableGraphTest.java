package it.matteobarbera.tablereservation.model;

import it.matteobarbera.tablereservation.model.table.CustomTable;
import it.matteobarbera.tablereservation.model.table.TableDefinition;
import it.matteobarbera.tablereservation.model.table.TableGraph;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class TableGraphTest {

    @Test
    public void instantiationTest() {
        TableDefinition tableDefinition1 = new TableDefinition("Tavolo grande", 6);
        TableDefinition tableDefinition2 = new TableDefinition("Tavolo piccolo", 4);
        CustomTable[] tables = {
                new CustomTable(4, tableDefinition2),
                new CustomTable(5, tableDefinition2),
                new CustomTable(2, tableDefinition1),
                new CustomTable(3, tableDefinition1),
                new CustomTable(1, tableDefinition1),
                new CustomTable(6, tableDefinition2)
        };
        var tableList = Arrays.asList(tables);
        var tableGraph = new TableGraph(tableList);



        System.out.println(Arrays.toString(tableGraph.getTablesSortedByCapacity()));
    }
}
