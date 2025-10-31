package it.matteobarbera.tablereservation.http;

public enum LayoutAPIInfo {
    LAYOUT_CREATED_OK("Layout with name %s was created successfully"),;



    private final String template;
    LayoutAPIInfo(String template) {
        this.template = template;
    }
    public String getMessage(Object... args){
        return String.format(template, args);
    }

}
