package it.matteobarbera.tablereservation.http.response;

public final class CommonBodies {
    private CommonBodies(){

    }

    public static String fromStatusAndMsg(int status, String message) {
        return "{\"status\":" + status + ", \"message\":\"" + message + "\"}";
    }

}
