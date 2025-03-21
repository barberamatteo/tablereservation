package it.matteobarbera.tablereservation;

public abstract class Constants {

    public static final String TOKEN = "token";
    public static final String UPDATE = "update";




    private static final String ADMIN_TABLES_CRUD_API_VERSION =
            "/api/v1.0.0";

    private static final String ADMIN_TABLESDEFINITION_CRUD_API_VERSION =
            "/api/v1.0.0";

    private static final String USER_RESERVATION_API_VERSION =
            "/api/v1.0.0";

    public static final String ADMIN_TABLES_CRUD_API_ENDPOINT =
            ADMIN_TABLES_CRUD_API_VERSION + "/admin/tables";

    public static final String ADMIN_TABLESDEFINITION_CRUD_API_ENDPOINT =
            ADMIN_TABLESDEFINITION_CRUD_API_VERSION + "/admin/tablesdefinition";

    public static final String USER_RESERVATION_API_ENDPOINT =
            USER_RESERVATION_API_VERSION + "/user/reservation";
}
