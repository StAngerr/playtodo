package utils;

import models.Session;
import play.cache.AsyncCacheApi;

import java.util.concurrent.ExecutionException;

public class CacheManager {
    private static CacheManager instance;
    public AsyncCacheApi cache;
    final private String sessionPrefix = "session-";

    private CacheManager(AsyncCacheApi cache) {
        this.cache = cache;
    }

    public static CacheManager getInstance(AsyncCacheApi cache) {
        if (instance == null) {
            instance = new CacheManager(cache);
        }
        return instance;
    }

    public void setSessionItem(Session session) {
        cache.set(sessionPrefix + session.getSessionId(), session.getExpiration());
    }

    public boolean isSessionExists(String sessionId) throws ExecutionException, InterruptedException {
        return cache.getOptional(sessionPrefix + sessionId)
                .toCompletableFuture().get().isPresent();
    }

    public void removeSessionItem(String sessionId) {
        cache.remove(sessionPrefix + sessionId);
    }
}
