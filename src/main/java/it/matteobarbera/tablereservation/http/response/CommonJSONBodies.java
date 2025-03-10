package it.matteobarbera.tablereservation.http.response;

public final class CommonJSONBodies {
    private CommonJSONBodies(){

    }

    public static String fromStatusAndMsg(int status, String message) {
        return "{\"status\":" + status + ", \"message\":\"" + message + "\"}";
    }

}
