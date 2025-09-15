package it.matteobarbera.tablereservation.cache;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * Represents a cache entry,in which obj is the domain object to process, and action is an action
 * to be performed on obj when the token is intended to be consumed.
 * @param obj A domain object
 * @param action An action
 * @param <T> The object type
 */
public record ActionCacheEntry<T>(T obj, String action) {
    public static final Class<CacheConstants> constantsClass = CacheConstants.class;

    /**
     * Checks whether the action parameter is a valid action.
     * @param action The string representing the action
     * @return true if valid, false otherwise
     */
    private boolean isActionValid(String action) {
        try {
            for (Field constant : constantsClass.getDeclaredFields()) {
                if (Objects.equals(constant.get(null), action)) {
                    return true;
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public ActionCacheEntry(T obj, String action) {
        this.obj = obj;
        if (isActionValid(action)) {
            this.action = action;
        } else {
            throw new RuntimeException("No action with value " + action + " declared in CacheConstants");
        }
    }

    public Class<?> getObjClass() {
        return obj.getClass();
    }


}
