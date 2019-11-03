package services;

import enums.UserRoles;
import models.Session;
import models.User;
import play.cache.AsyncCacheApi;
import play.mvc.Http;

import java.util.List;

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

    public Session validateSessionAndUser(Http.Request request) throws Exception {
        Session session = sessionService.validateSession(request);
        User user =  getAndValidateUser(session.getUserId());

        session.setUser(user);
        return session;
    }

    public Session validateSessionAndUser(Http.Request request, List<UserRoles> roles) throws Exception {
        Session session = sessionService.validateSession(request);
        User user = userService.getUserById(session.getUserId());
        userService.validatePermissions(user, roles);
        session.setUser(user);
        return session;
    }

    private User getAndValidateUser(String userId) throws Exception {
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new Exception("Invalid session.");
        }
        return user;
    }
}
