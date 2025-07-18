package it.matteobarbera.tablereservation.cache;

import it.matteobarbera.tablereservation.cache.model.ActionCacheEntry;
import it.matteobarbera.tablereservation.log.CacheLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static it.matteobarbera.tablereservation.log.CacheLog.ACTION_TOKEN_CREATED;

@Component
public class CacheUtils {


    private static final Logger log = LoggerFactory.getLogger(CacheUtils.class);
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
        log.atInfo().log(ACTION_TOKEN_CREATED, token, obj, action);
        return token.toString();
    }

    public ActionCacheEntry<?> getActionCacheEntryBoundToToken(String token){
        Cache cache = getTokenCache();
        UUID tokenUUID = UUID.fromString(token);
        ActionCacheEntry<?> toRet = cache.get(tokenUUID, ActionCacheEntry.class);
        cache.evict(tokenUUID);
        log.atInfo().log(CacheLog.ACTION_TOKEN_CONSUMED, token);
        return toRet;
    }


}
