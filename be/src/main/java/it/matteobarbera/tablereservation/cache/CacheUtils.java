package it.matteobarbera.tablereservation.cache;

import it.matteobarbera.tablereservation.cache.model.ActionCacheEntry;
import it.matteobarbera.tablereservation.model.reservation.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Component
public class CacheUtils {


    private final ConcurrentMapCacheManager cacheManager;

    @Autowired
    public CacheUtils(ConcurrentMapCacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }




    private Cache getTokenCache(){
        return cacheManager.getCache(CacheConstants.TOKEN_CACHE);
    }

    public String createActionTokenBoundToObj(String action, Object obj){
        UUID token = UUID.randomUUID();
        ActionCacheEntry<Object> entry = new ActionCacheEntry<>(obj, action);
        getTokenCache().put(token, entry);
        return token.toString();
    }

    public ActionCacheEntry<?> getActionCacheEntryBoundToToken(String token){
        Cache cache = getTokenCache();
        UUID tokenUUID = UUID.fromString(token);
        return cache.get(tokenUUID, ActionCacheEntry.class);

        /*
         * TODO: Understand why CacheWrapper.get() is null
         */
    }


}
