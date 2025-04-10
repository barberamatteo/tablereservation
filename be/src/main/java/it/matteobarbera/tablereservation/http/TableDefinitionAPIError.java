package it.matteobarbera.tablereservation.http;

public enum TableDefinitionAPIError {
    EXISTING_CATEGORY_NAME("A table definition with the category name %s already exists.");

    private final String template;
    TableDefinitionAPIError(String template) {
        this.template = template;
    }

    public String getMessage(Object... args){
        return String.format(template, args);
    }
}
