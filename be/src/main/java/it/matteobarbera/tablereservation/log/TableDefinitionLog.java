package it.matteobarbera.tablereservation.log;

public class TableDefinitionLog {
    private TableDefinitionLog() {}

    public static final String DEFINITION_ALREADY_EXISTS = "Tried to define an already existing table definition with name {}";
    public static final String NO_DEFINITION_FOUND = "Tried to get all table definitions. Zero found.";
    public static final String ALL_DEFINITIONS_FOUND = "Returning all {} table definitions.";
    public static final String DEFINITION_CREATED = "Created table definition with name {} and standalone capacity {}";
}
