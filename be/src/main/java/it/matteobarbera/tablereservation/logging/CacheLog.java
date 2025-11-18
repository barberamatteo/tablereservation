package it.matteobarbera.tablereservation.logging;

public final class CacheLog {
    private CacheLog() {

    }

    public static String ACTION_TOKEN_CREATED = "Created a token {} bound to task {}";
    public static String ACTION_TOKEN_CONSUMED = "The action token {} has been consumed by the user." +
            "It'll be evicted from action token cache.";


}
