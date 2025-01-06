package it.matteobarbera.tablereservation.model.table.admin;

import it.matteobarbera.tablereservation.AbstractBadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.lang.NonNull;
import org.springframework.web.server.ResponseStatusException;

public class TableDefinitionCRUDException extends AbstractBadRequestException {

    public enum Cause{
        EXISTING_CATEGORY_NAME
    }

    protected final Cause cause;

    public TableDefinitionCRUDException(HttpStatus status, Object metadata, Cause cause) {
        super(status, metadata);
        this.cause = cause;
    }

    @NonNull
    @Override
    public String getMessage() {
        switch (cause) {
            case EXISTING_CATEGORY_NAME:
                return "A table definition with the category name " + metadata + " already exists.";
            default:
                return "Unexpected error occurred while creating the table definition.";
        }
    }

    @Override
    public Cause getCauseType() {
        return cause;
    }
}
