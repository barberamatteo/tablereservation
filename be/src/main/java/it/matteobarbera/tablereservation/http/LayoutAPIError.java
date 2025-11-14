package it.matteobarbera.tablereservation.http;

public enum LayoutAPIError {
    NO_TABLES_FOUND("No tables found in adjacency map"),
    INVALID_ADJACENCY_RULE("An invalid adjacency rule was found. Please review your layout."),
    GENERIC_ERROR("An unexpected error occurred. Please try later."),
    NO_SUCH_LAYOUT_WITH_ID("No such layout with id %d");


    private final String template;
    LayoutAPIError(String template) {
        this.template = template;
    }
    public String getMessage(Object... args) {
        return String.format(template, args);
    }
}
