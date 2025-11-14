package it.matteobarbera.tablereservation.model.table.layout;

import it.matteobarbera.tablereservation.AbstractBadRequestException;
import org.springframework.http.HttpStatusCode;
import org.springframework.lang.NonNull;

import java.util.Collection;

public class UnjoinableTableException extends AbstractBadRequestException {

    public enum Cause{
        UNJOINABLE_TABLE_FOUND,
    }

    protected final Cause cause;

    public UnjoinableTableException(HttpStatusCode status, Object metadata, Cause cause) {
        super(status, metadata);
        this.cause = cause;
    }

    public UnjoinableTableException(Object metadata, Cause cause) {
        super(metadata);
        this.cause = cause;
    }

    @NonNull
    @Override
    public String getMessage() {
        switch (cause) {
            case UNJOINABLE_TABLE_FOUND:
                if (!(metadata instanceof Collection<?>))
                    return "Table " + metadata + "doesn't implement Joinable, hence it's not intended to be joinable." +
                            " Can't create table layout";
                else
                    return "One or more tables in " + metadata + " don't implement Joinable, hence they're not intended to be joinable." +
                        " Can't create table layout.";
            default:
                return "Unexpected error occurred while creating the table layout";
        }
    }

    @Override
    public Enum<?> getCauseType() {
        return null;
    }
}
