package services;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSObject;
import models.Session;
import models.User;
import models.dto.SessionJwtDTO;
import net.minidev.json.JSONObject;
import play.cache.AsyncCacheApi;
import play.mvc.Http;
import utils.CacheManager;
import utils.JwtHelper;
import utils.errorHandler.InvalidToken;
import utils.errorHandler.SessionExpired;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.text.ParseException;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Singleton
public class SessionService {
    private static CacheManager cacheManager;
    private static JwtHelper jwtHelper;

    @Inject
    private SessionService(AsyncCacheApi cache)  {
        cacheManager = CacheManager.getInstance(cache);
        jwtHelper = JwtHelper.getInstance();
    }

    public Session createNewSession(User user) {
        String sessionId = UUID.randomUUID().toString();
        Session session = new Session(sessionId, user.getId());
        try {
            String token = jwtHelper.generateJwt(session.getDataForJwt());
            session.setJwt(token);
            session.setUser(user);
            cacheManager.setSessionItem(session);
        } catch (JOSEException | IllegalAccessException e) {
            System.out.println(e);
        }
        return session;
    }

    public Session validateSession(Http.Request request) throws SessionExpired, InvalidToken, ParseException, ExecutionException, InterruptedException {
        Optional<String> header = request.getHeaders().get("Authorization");
        if (header.isPresent()) {
            String token = header.get();
            JWSObject obj =  jwtHelper.parse(token);
            if (obj == null || obj.getPayload().toJSONObject() == null) {
                throw new InvalidToken();
            }

            SessionJwtDTO sessionDTO = parseSessionDto(obj.getPayload().toJSONObject());

            if (!cacheManager.isSessionExists(sessionDTO.sessionId)) {
                throw new InvalidToken();
            }

            if (isExpired(sessionDTO.expiration)) {
                cacheManager.removeSessionItem(sessionDTO.sessionId);
                throw new SessionExpired();
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
