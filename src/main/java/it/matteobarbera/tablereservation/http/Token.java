package it.matteobarbera.tablereservation.http;

public class Token<T> {
    private final T object;

    public Token(T object){
        this.object = object;
    }

    public T getValue(){
        return object;
    }


}
