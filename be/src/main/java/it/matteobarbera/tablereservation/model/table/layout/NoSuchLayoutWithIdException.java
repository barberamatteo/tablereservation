package it.matteobarbera.tablereservation.model.table.layout;

import it.matteobarbera.tablereservation.AbstractBadRequestException;
import org.springframework.http.HttpStatusCode;

public class NoSuchLayoutWithIdException extends AbstractBadRequestException {

    public enum Cause {
        NO_SUCH_LAYOUT_WITH_ID,
    }

    protected final Cause cause;



    public NoSuchLayoutWithIdException(HttpStatusCode status, Object metadata, Cause cause) {
        super(status, metadata);
        this.cause = cause;
    }

    public NoSuchLayoutWithIdException(Object metadata, Cause cause) {
        super(metadata);
        this.cause = cause;
    }


    @Override
    public String getMessage() {
        switch (cause) {
            case NO_SUCH_LAYOUT_WITH_ID:
                return "No such layout with id " + metadata + " found.";
            default:
                return "An unexpected error occurred";
        }
    }

    @Override
    public Enum<?> getCauseType() {
        return null;
    }
}
