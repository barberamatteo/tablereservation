package it.matteobarbera.tablereservation.cache.model;

import it.matteobarbera.tablereservation.cache.CacheConstants;

import java.lang.reflect.Field;
import java.util.Objects;

public record ActionCacheEntry<T>(T obj, String action) {
    public static final Class<CacheConstants> constantsClass = CacheConstants.class;

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
