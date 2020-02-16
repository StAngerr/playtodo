package services;

import enums.UserRoles;
import models.Session;
import models.User;
import play.cache.AsyncCacheApi;
import play.mvc.Http;
import utils.errorHandler.*;

import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RequestValidationService {
    private static RequestValidationService instance;
    private SessionService sessionService;
    private UserService userService;

    private RequestValidationService(AsyncCacheApi cache) {
        sessionService = SessionService.getInstance(cache);
        userService = UserService.getInstance();
    }

    public static RequestValidationService getInstance(AsyncCacheApi cache) {
        if (instance == null) {
            instance = new RequestValidationService(cache);
        }
        return instance;
    }

    public Session validateSessionAndUser(Http.Request request) throws SessionExpired, InvalidToken, InvalidSession {
        Session session;
        try {
            session = sessionService.validateSession(request);
        } catch (ParseException | ExecutionException | InterruptedException e) {
            throw new InvalidSession();
        }
        User user =  getAndValidateUser(session.getUserId());

        session.setUser(user);
        return session;
    }

    public Session validateSessionAndUser(Http.Request request, List<UserRoles> roles) throws ErrorReadingUserStorage, SessionExpired, InvalidToken, NoPermission, InvalidSession {
        try {
            Session session = sessionService.validateSession(request);
            User user = userService.getUserById(session.getUserId());
            userService.validatePermissions(user, roles);
            session.setUser(user);
            return session;
        } catch ( ExecutionException | InterruptedException | ParseException e) {
            throw new InvalidSession();
        }
    }

    private User getAndValidateUser(String userId) throws InvalidSession {
        try {
            User user = userService.getUserById(userId);
            if (user == null) {
                throw new InvalidSession();
            }
            return user;
        } catch (ErrorReadingUserStorage e) {
            throw new InvalidSession();
        }
    }
}
