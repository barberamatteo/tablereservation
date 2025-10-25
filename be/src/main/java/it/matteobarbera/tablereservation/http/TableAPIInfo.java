package it.matteobarbera.tablereservation.http;

import it.matteobarbera.tablereservation.model.table.AbstractTable;
import it.matteobarbera.tablereservation.model.table.SimpleJoinableTable;
import it.matteobarbera.tablereservation.model.table.SimpleTable;

public enum TableAPIInfo {

    TABLE_CREATED("Simple table created with number %d and category %s"),
    JOINABLE_TABLE_CREATED("Simple joinable table created with number %d, category %s, headCapacity %d and joiningCapacity %d");
    private final String template;
    TableAPIInfo(String template) {
        this.template = template;
    }

    public static String getMessage(AbstractTable table) {
        if (table instanceof SimpleTable)
            return TABLE_CREATED.getMessage(
                    table.getNumberInLounge(),
                    table.getTableDefinition().getCategoryName()
            );
        if (table instanceof SimpleJoinableTable simpleJoinableTable)
            return JOINABLE_TABLE_CREATED.getMessage(
                    simpleJoinableTable.getNumberInLounge(),
                    simpleJoinableTable.getTableDefinition().getCategoryName(),
                    simpleJoinableTable.getHeadCapacity(),
                    simpleJoinableTable.getJoiningCapacity()
            );
        return "";
    }
    public String getMessage(Object... args){
        return String.format(template, args);
    }
}
