package services;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSObject;
import models.Session;
import models.dto.SessionJwtDTO;
import net.minidev.json.JSONObject;
import play.cache.AsyncCacheApi;
import play.mvc.Http;
import utils.CacheManager;
import utils.JwtHelper;

import java.util.Date;
import java.util.Optional;
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

    public Session createNewSession(String userId) {
        String sessionId = UUID.randomUUID().toString();
        Session session = new Session(sessionId, userId);
        try {
            String token = jwtHelper.generateJwt(session.getDataForJwt());
            session.setJwt(token);
            cacheManager.setSessionItem(session);
        } catch (JOSEException | IllegalAccessException e) {
            System.out.println(e);
        }
        return session;
    }

    public Session validateSession(Http.Request request) throws Exception {
        Optional<String> header = request.getHeaders().get("Authorization");
        if (header.isPresent()) {
            String token = header.get();
            JWSObject obj =  jwtHelper.parse(token);
            SessionJwtDTO sessionDTO = parseSessionDto(obj.getPayload().toJSONObject());

            if (!cacheManager.isSessionExists(sessionDTO.sessionId)) {
                throw new Exception("Invalid token.");
            }

            if (isExpired(sessionDTO.expiration)) {
                cacheManager.removeSessionItem(sessionDTO.sessionId);
                throw new Exception("Session expired.");
            }

            return new Session(sessionDTO.sessionId, sessionDTO.userId, sessionDTO.expiration);
        }
        return null;
    }

    public SessionJwtDTO parseSessionDto(JSONObject payload) {
        String sessionId = payload.get("sessionId").toString();
        String userId =  payload.get("userId").toString();
        String expiration =  payload.get("expiration").toString();
        return new SessionJwtDTO(sessionId, userId, Long.parseLong(expiration));
    }

    public boolean isExpired(long expirationTime) {
        int minutes = 3;
        return (new Date().getTime() - expirationTime) / 1000 / 60 > minutes;
    }
}
