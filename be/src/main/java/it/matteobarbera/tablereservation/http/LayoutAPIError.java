package it.matteobarbera.tablereservation.http;

public enum LayoutAPIError {
    NO_TABLES_FOUND("No tables found in adjacency map"),
    INVALID_ADJACENCY_RULE("An invalid adjacency rule was found. Please review your layout."),
    GENERIC_ERROR("An unexpected error occurred. Please try later.");


    private final String template;
    LayoutAPIError(String template) {
        this.template = template;
    }
    public String getMessage(Object... args) {
        return String.format(template, args);
    }
}
