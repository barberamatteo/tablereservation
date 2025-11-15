package it.matteobarbera.tablereservation.http;

public enum CustomerAPIInfo {
    CUSTOMER_FETCHED_OK("Customer with id %d was fetched successfully"),
    CUSTOMER_CREATED("Customer with phoneNumber %s created");


    private final String template;
    CustomerAPIInfo(String template) {
        this.template = template;
    }

    public String getMessage(Object... args){
        return String.format(template, args);
    }
}
