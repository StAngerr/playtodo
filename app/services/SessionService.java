package services;

import com.nimbusds.jose.JOSEException;
import models.Credentials;
import models.Session;
import play.cache.AsyncCacheApi;
import utils.CacheManager;
import utils.JwtHelper;

import java.util.UUID;

public class SessionService {
    private static SessionService instance;
    private static CacheManager cacheManager;
    private static JwtHelper jwtHelper;

    private SessionService(AsyncCacheApi cache)  {
        cacheManager = CacheManager.getInstance(cache);
        jwtHelper = JwtHelper.getInstance();
    }

    public static SessionService getInstance(AsyncCacheApi cache) {
        if (instance == null) {
            instance = new SessionService(cache);
        }
        return instance;
    }

    public Session createNewSession() {
        String sessionId = UUID.randomUUID().toString();
        Session session = new Session(sessionId);
        try {
            String token = jwtHelper.generateJwt(session.getDataForJwt());
            session.setJwt(token);
            cacheManager.setSessionItem(session);
        } catch (JOSEException | IllegalAccessException e) {
            System.out.println(e);
        }
        return session;
    }
}
