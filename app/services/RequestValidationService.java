package services;

import enums.UserRoles;
import models.Session;
import models.User;
import play.cache.AsyncCacheApi;
import play.mvc.Http;
import utils.errorHandler.InvalidSession;
import utils.errorHandler.InvalidToken;
import utils.errorHandler.NoPermission;
import utils.errorHandler.SessionExpired;

import java.io.IOException;
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

    public Session validateSessionAndUser(Http.Request request) throws SessionExpired, ExecutionException, InvalidToken, ParseException, InterruptedException, org.json.simple.parser.ParseException, InvalidSession, IOException {
        Session session = sessionService.validateSession(request);
        User user =  getAndValidateUser(session.getUserId());

        session.setUser(user);
        return session;
    }

    public Session validateSessionAndUser(Http.Request request, List<UserRoles> roles) throws SessionExpired, ExecutionException, InvalidToken, ParseException, InterruptedException, NoPermission, IOException, org.json.simple.parser.ParseException {
        Session session = sessionService.validateSession(request);
        User user = userService.getUserById(session.getUserId());
        userService.validatePermissions(user, roles);
        session.setUser(user);
        return session;
    }

    private User getAndValidateUser(String userId) throws InvalidSession, IOException, org.json.simple.parser.ParseException {
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new InvalidSession();
        }
        return user;
    }
}
