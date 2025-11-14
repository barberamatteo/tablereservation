package it.matteobarbera.tablereservation.http.request;

import it.matteobarbera.tablereservation.AbstractBadRequestException;

public class IncompleteIdsMapException extends AbstractBadRequestException {

    public enum Cause {
        INCOMPLETE_IDS_MAP,
    }

    protected final Cause cause;

    public IncompleteIdsMapException(Object metadata, Cause cause) {
        super(metadata);
        this.cause = cause;
    }


    @Override
    public Enum<?> getCauseType() {
        return null;
    }

    @Override
    public String getMessage() {
        switch (cause) {
            case INCOMPLETE_IDS_MAP:
                return "Detected a missing entry in map. All the tables must be specified in the keySet." +
                        " Received map was: " + metadata.toString();
            default:
                return "An unexpected error occurred.";
        }
    }
}
