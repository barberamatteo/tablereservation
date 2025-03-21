package it.matteobarbera.tablereservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

import static it.matteobarbera.tablereservation.Constants.UPDATE;

@Component
public class CacheUtils {


    private final ConcurrentMapCacheManager cacheManager;

    @Autowired
    public CacheUtils(ConcurrentMapCacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }



    public Cache getUpdateTokenCache(){
        return cacheManager.getCache("updateToken");

    }

    public String createUpdateTokenBoundTo(Object obj){
        UUID token = UUID.randomUUID();

        Objects.requireNonNull(
                cacheManager.getCache("actionTokens")
        ).put(token, UPDATE);

        getUpdateTokenCache().put(token, obj);
        return token.toString();
    }



}
