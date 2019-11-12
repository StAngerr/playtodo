package services;

import enums.UserRoles;
import models.Session;
import models.User;
import play.cache.AsyncCacheApi;
import play.mvc.Http;
import utils.errorHandler.*;

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

    public Session validateSessionAndUser(Http.Request request) throws SessionExpired, InvalidToken, InvalidSession {
        Session session = null;
        try {
            session = sessionService.validateSession(request);
        } catch (ParseException | ExecutionException | InterruptedException e) {
            throw new InvalidSession();
        }
        User user =  getAndValidateUser(session.getUserId());

        session.setUser(user);
        return session;
    }

    public Session validateSessionAndUser(Http.Request request, List<UserRoles> roles) throws SessionExpired, InvalidToken, NoPermission, UserNotFound, InvalidSession {
        try {
            Session session = sessionService.validateSession(request);
            User user = userService.getUserById(session.getUserId());
            userService.validatePermissions(user, roles);
            session.setUser(user);
            return session;
        } catch ( ExecutionException | InterruptedException | ParseException | org.json.simple.parser.ParseException e) {
            throw new InvalidSession();
        } catch (IOException e) {
            throw new UserNotFound();
        }
    }

    private User getAndValidateUser(String userId) throws InvalidSession {
        try {
            User user = userService.getUserById(userId);
            if (user == null) {
                throw new InvalidSession();
            }
            return user;
        } catch (IOException | org.json.simple.parser.ParseException e) {
            throw new InvalidSession();
        }
    }
}
