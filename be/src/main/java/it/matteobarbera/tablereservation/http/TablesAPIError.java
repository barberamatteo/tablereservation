package it.matteobarbera.tablereservation.http;

public enum TablesAPIError {
    NO_SUCH_CATEGORY_DEFINED("Category %s does not exist. Create a new table definition first."),
    NUMBER_CONFLICT("Table number %d already exists. Please choose another number.");

    private final String template;
    TablesAPIError(String template) {
        this.template = template;
    }

    public String getMessage(Object... args){
        return String.format(template, args);
    }
}
