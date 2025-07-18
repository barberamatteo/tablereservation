package it.matteobarbera.tablereservation.http.response;

import java.util.Map;

public final class CommonJSONBodies {
    private CommonJSONBodies(){

    }

    public static String fromStatusAndMsg(int status, String message) {
        return "{\"status\":" + status + ", \"message\":\"" + message + "\"}";
    }

    public static String fromStatusAndMsgAndX(int status, String message, Map<String, String> additional) {
        StringBuilder partial = new StringBuilder(
                "{\"status\":" + status + ", \"message\":\"" + message + "\","
        );
        for (Map.Entry<String, String> entry : additional.entrySet()) {
            partial.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue()).append("\",");
        }
        partial.deleteCharAt(partial.length() - 1);
        partial.append("}");
        return partial.toString();
    }

}
