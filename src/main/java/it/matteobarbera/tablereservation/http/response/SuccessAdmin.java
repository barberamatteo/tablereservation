package it.matteobarbera.tablereservation.http.response;

public final class SuccessAdmin {

    private SuccessAdmin(){
    }

    public static String getMessageForAction(String action) {
        switch (action){
            case "createTable":
                return "Table successfully created";
            case "createDefinitionTable":
                return "Table definition created successfully";
            default:
                return "Action not recognized.";
        }
    }
}
