package it.matteobarbera.tablereservation.log;

public final class CacheLog {
    private CacheLog() {

    }

    public static String ACTION_TOKEN_CREATED = "Created an action token {} bound to {} for action {}";
    public static String ACTION_TOKEN_CONSUMED = "The action token {} has been consumed by the user." +
            "It'll be evicted from action token cache.";


}
