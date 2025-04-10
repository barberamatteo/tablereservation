package it.matteobarbera.tablereservation.http;

public enum TableDefinitionAPIInfo {
    TABLE_DEFINITION_CREATED("Table definition with name %s and standalone capacity %d created");

    private final String template;
    TableDefinitionAPIInfo(String template) {
        this.template = template;
    }

    public String getMessage(Object... args){
        return String.format(template, args);
    }

}
