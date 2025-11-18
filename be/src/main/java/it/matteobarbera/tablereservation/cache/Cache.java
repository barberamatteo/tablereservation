package it.matteobarbera.tablereservation.cache;

import it.matteobarbera.tablereservation.logging.CacheLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static it.matteobarbera.tablereservation.logging.CacheLog.ACTION_TOKEN_CREATED;


@Component
public class Cache {


    private static final Logger log = LoggerFactory.getLogger(Cache.class);
    private final ConcurrentMapCacheManager cacheManager;

    @Autowired
    public Cache(ConcurrentMapCacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }




    private org.springframework.cache.Cache getTokenCache(){
        return cacheManager.getCache(CacheConstants.TOKEN_CACHE);
    }

    /**
     * Creates a random UUID associated with a domain object. Then it saves the token to the token cache
     * @param task a task object
     * @return the generated UUID
     */
    public String createTokenBoundToTask(AbstractTask task){
        UUID token = UUID.randomUUID();
        getTokenCache().put(token, task);
        log.atInfo().log(ACTION_TOKEN_CREATED, token, task);
        return token.toString();
    }

    /**
     * Returns the ActionCacheEntry object bound to the token, then it consumes the token by freeing the cache
     * @param token the token of the required obj
     * @return an ActionCacheEntry record
     */
    public ActionCacheEntry<?> getTaskFromToken(String token){
        org.springframework.cache.Cache cache = getTokenCache();
        UUID tokenUUID = UUID.fromString(token);
        ActionCacheEntry<?> toRet = cache.get(tokenUUID, ActionCacheEntry.class);
        cache.evict(tokenUUID);
        log.atInfo().log(CacheLog.ACTION_TOKEN_CONSUMED, token);
        return toRet;
    }


}
