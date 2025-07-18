package it.matteobarbera.tablereservation.log;

public final class CustomerLog {
    private CustomerLog() {}


    public static String NO_CUSTOMER_FOUND = "No customer was found";
    public static String CUSTOMER_FOUND = "Returning customer with id {}";
    public static String ALL_CUSTOMERS_FOUND = "Returning all {} customers";
    public static String FOUND_CUSTOMERS_MATCHING = "Found {} customers matching phone number {}";
}
