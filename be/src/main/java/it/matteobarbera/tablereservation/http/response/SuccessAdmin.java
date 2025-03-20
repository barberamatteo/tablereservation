package it.matteobarbera.tablereservation.http.response;

public final class SuccessAdmin {

    private SuccessAdmin(){
    }

    public static String getMessageForAction(String action) {
        return switch (action) {
            case "createTable" -> "Table successfully created";
            case "createDefinitionTable" -> "Table definition created successfully";
            default -> "Action not recognized.";
        };
    }
}
