package it.matteobarbera.tablereservation.model.customer;

import it.matteobarbera.tablereservation.AbstractBadRequestException;
import org.hibernate.type.descriptor.jdbc.ObjectNullResolvingJdbcType;
import org.springframework.http.HttpStatusCode;

public class NoSuchCustomerWithIdException extends AbstractBadRequestException {

    public enum Cause {
        NO_SUCH_CUSTOMER_WITH_ID,
    }


    protected final Cause cause;

    public NoSuchCustomerWithIdException(HttpStatusCode status, Object metadata, Cause cause) {
        super(status, metadata);
        this.cause = cause;
    }


    public NoSuchCustomerWithIdException(Object metadata, Cause cause) {
        super(metadata);
        this.cause = cause;
    }

    @Override
    public String getMessage() {
        switch (cause) {
            case NO_SUCH_CUSTOMER_WITH_ID:
                return "No such customer with id " + metadata + " found.";
            default:
                return "An unexpected error occurred";
        }
    }

    @Override
    public Enum<?> getCauseType() {
        return null;
    }
}
