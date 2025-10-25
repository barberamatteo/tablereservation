package it.matteobarbera.tablereservation.logging;

public final class TableLog {
    private TableLog() {}

    public static final String ALL_TABLES_FOUND = "Returning all {} tables";
    public static final String NO_TABLES_FOUND = "No tables found";
    public static final String TABLE_FOUND = "Returning table with id {}";
    public static final String NO_TABLE_FOUND_WITH_NUMBER = "No table found with number {}";
    public static final String NO_CATEGORY_DEFINED = "Tried to create a new table with non-existing category {}.";
    public static final String SIMPLE_TABLE_CREATED = "Simple table created with number {} and category {}";
    public static final String SIMPLE_JOINABLE_TABLE_CREATED = "Simple joinable table created with number {}, category {}, headCapacity {}, joiningCapacity {}";
    public static final String TABLE_WITH_SAME_NUMBER = "Tried to create a new table with an already existing table with number {}";
}
