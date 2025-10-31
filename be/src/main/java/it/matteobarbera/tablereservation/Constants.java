package it.matteobarbera.tablereservation;

public abstract class Constants {

    private static final String CUSTOMER_CRUD_API_VERSION =
            "/api/v1.0.0";

    private static final String ADMIN_TABLES_CRUD_API_VERSION =
            "/api/v1.0.0";

    private static final String ADMIN_TABLESDEFINITION_CRUD_API_VERSION =
            "/api/v1.0.0";

    private static final String USER_RESERVATION_API_VERSION =
            "/api/v1.0.0";

    private static final String LAYOUT_API_VERSION =
            "/api/v1.0.0";


    public static final String LAYOUT_API_ENDPOINT =
            LAYOUT_API_VERSION + "/layout";

    public static final String ADMIN_TABLES_CRUD_API_ENDPOINT =
            ADMIN_TABLES_CRUD_API_VERSION + "/admin/tables";

    public static final String ADMIN_TABLESDEFINITION_CRUD_API_ENDPOINT =
            ADMIN_TABLESDEFINITION_CRUD_API_VERSION + "/admin/tablesdefinition";

    public static final String USER_RESERVATION_API_ENDPOINT =
            USER_RESERVATION_API_VERSION + "/user/reservation";

    public static final String CUSTOMER_CRUD_API_ENDPOINT =
             CUSTOMER_CRUD_API_VERSION + "/customer";


    public static final String LOGIN_ENDPOINT = "/login";

    public static final String AUTH_UTILS_ENDPOINT = "/auth";
    public static final String POST = "POST";



    public static final String ROLE_ADMIN = "ADMIN";
}
