package it.matteobarbera.tablereservation.http;

public enum LayoutAPIError {
    NO_TABLES_FOUND("No tables found in adjacency map"),
    INVALID_ADJACENCY_RULE("An invalid adjacency rule was found. Please review your layout."),
    GENERIC_ERROR("An unexpected error occurred. Please try later."),
    NO_SUCH_LAYOUT_WITH_ID("No such layout with id %d"),
    NO_SUCH_LAYOUT_WITH_NAME("No such layout with name '%s'"),
    LAYOUT_WITH_NAME_ALREADY_EXISTS("A layout with the name '%s' already exists. Please choose another name"),;


    private final String template;
    LayoutAPIError(String template) {
        this.template = template;
    }
    public String getMessage(Object... args) {
        return String.format(template, args);
    }
}
