package it.matteobarbera.tablereservation.http;

public enum CustomersAPIError {
    TOO_FEW_DIGITS("THe input \"regex\" must be at least 3 digits long");

    private final String template;
    CustomersAPIError(String template) {
        this.template = template;
    }

    public String getMessage(Object... args){
        return String.format(template, args);
    }
}
