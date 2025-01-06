package it.matteobarbera.tablereservation.model.table.admin;

import it.matteobarbera.tablereservation.AbstractBadRequestException;
import org.springframework.http.HttpStatusCode;
import org.springframework.lang.NonNull;

public class TableCRUDException extends AbstractBadRequestException {

    public enum Cause {
        NO_SUCH_CATEGORY_DEFINED,
        TABLE_NOT_FOUND,
        NUMBER_CONFLICT
    }

    protected final Cause cause;

    public TableCRUDException(HttpStatusCode status, Object metadata, Cause cause) {
        super(status, metadata);
        this.cause = cause;
    }


    @NonNull
    @Override
    public String getMessage() {
        switch (cause){
            case NUMBER_CONFLICT:
                return "Table number " + metadata + " already exists. Please choose another number.";
            case NO_SUCH_CATEGORY_DEFINED:
                return "Category " + metadata + " does not exist. Create a new table definition first.";
            case TABLE_NOT_FOUND:
                return "Table " + metadata + " does not exist.";
            default:
                return "Unexpected error occurred while creating the table.";
        }
    }

    @Override
    public Cause getCauseType() {
        return cause;
    }
}
