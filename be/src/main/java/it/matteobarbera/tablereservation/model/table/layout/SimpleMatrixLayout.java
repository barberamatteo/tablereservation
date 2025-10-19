package it.matteobarbera.tablereservation.model.table.layout;

import it.matteobarbera.tablereservation.model.table.AbstractTable;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SimpleMatrixLayout extends AbstractTableLayout {


    public SimpleMatrixLayout(Set<AbstractTable> tables) {
        super(tables);
    }


}
