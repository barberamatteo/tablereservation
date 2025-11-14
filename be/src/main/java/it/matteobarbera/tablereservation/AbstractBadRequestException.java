package it.matteobarbera.tablereservation;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public abstract class AbstractBadRequestException extends ResponseStatusException   {
    protected final Object metadata;


    public AbstractBadRequestException(HttpStatusCode status, Object metadata) {
        super(status);
        this.metadata = metadata;
    }

    public AbstractBadRequestException(Object metadata){
        super(HttpStatus.BAD_REQUEST);
        this.metadata = metadata;
    }

    public abstract Enum<?> getCauseType();

}
