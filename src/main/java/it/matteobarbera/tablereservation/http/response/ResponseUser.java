package it.matteobarbera.tablereservation.http.response;

import java.util.HashMap;

public abstract class ResponseUser{

    protected HashMap<String, Object> metadata;

    public ResponseUser() {
        this.metadata = new HashMap<>();
    }

    public final class SuccessUser extends ResponseUser{
        public SuccessUser() {
            super();
        }

        public static String getMessageForAction(String action){
            return null;
        }
    }

    public final class ErrorUser extends ResponseUser{
        public ErrorUser() {}

        public static String getMessageForAction(String action){
            return null;
        }
    }



}
