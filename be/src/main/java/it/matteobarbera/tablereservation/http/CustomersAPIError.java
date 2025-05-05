package it.matteobarbera.tablereservation.http;

public enum CustomersAPIError {
    TOO_FEW_DIGITS("The input \"regex\" must be at least 3 digits long"),
    CUSTOMER_WITH_EMAIL_ALREADY_EXISTS("The specified email %s is already associated with an existing customer." +
                                            "Please use another email."),
    CUSTOMER_WITH_PHONE_NUMBER_ALREADY_EXISTS("The specified phone number %s is already associated with an existing customer." +
                                         "Please use another phone number");
    private final String template;
    CustomersAPIError(String template) {
        this.template = template;
    }

    public String getMessage(Object... args){
        return String.format(template, args);
    }
}
