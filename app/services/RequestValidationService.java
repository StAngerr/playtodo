package services;

import enums.UserRoles;
import models.Session;
import models.User;
import play.cache.AsyncCacheApi;
import play.mvc.Http;
import utils.errorHandler.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Singleton
public class RequestValidationService {
    private SessionService sessionService;
    private UserService userService;

    @Inject
    private RequestValidationService(UserService userService, SessionService sessionService) {
        this.sessionService = sessionService;
        this.userService = userService;
    }

    public Session validateSessionAndUser(Http.Request request) throws SessionExpired, InvalidToken, NoPermission, InvalidSession {
        return validateSessionAndUser(request, Collections.emptyList());
    }

    public Session validateSessionAndUser(Http.Request request, List<UserRoles> roles) throws SessionExpired, InvalidToken, NoPermission, InvalidSession {
        try {
            Session session = sessionService.validateSession(request);
            User user = userService.getUserById(session.getUserId());
            if (!roles.isEmpty()) {
                userService.validatePermissions(user, roles);
            }
            session.setUser(user);
            return session;
        } catch ( ExecutionException | InterruptedException | ParseException | ErrorReadingUserStorage e) {
            throw new InvalidSession(e);
        }
    }
}
