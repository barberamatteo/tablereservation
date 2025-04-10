package it.matteobarbera.tablereservation.http;

public enum TableAPIInfo {

    TABLE_CREATED("Table created with number %d and category %s"),;

    private final String template;
    TableAPIInfo(String template) {
        this.template = template;
    }

    public String getMessage(Object... args){
        return String.format(template, args);
    }
}
