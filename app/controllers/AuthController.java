package controllers;

import com.google.inject.Inject;
import enums.UserRoles;
import models.Credentials;
import models.Session;
import models.User;
import play.cache.AsyncCacheApi;
import play.mvc.*;
import services.SessionService;
import services.UserService;
import utils.HttpHelper;
import utils.JwtHelper;
import utils.ValidationHelper;

import java.util.Date;

import static play.mvc.Results.*;

public class AuthController {
    private HttpHelper httpHelper;
    private JwtHelper jwtHelper;
    private SessionService sessionService;
    private UserService userService;

    @Inject
    public AuthController(AsyncCacheApi cache) {
        this.httpHelper = HttpHelper.getInstance();
        this.jwtHelper = JwtHelper.getInstance();
        this.sessionService = SessionService.getInstance(cache);
        this.userService = UserService.getInstance();
    }

    // returns jwt + current user
    public Result login(Http.Request request) {
        try {
            Credentials loginCredentials = httpHelper.getLoginData(request);
            ValidationHelper.validateLoginCredentials(loginCredentials);
            User user = userService.getAndValidateUser(loginCredentials);
            Session session = sessionService.createNewSession(user.id);
            session.setUser(user);
            return ok(session.asJson().toJSONString());
        } catch (Exception e) {
            return badRequest(e.getMessage());
        }
    }

    // returns jwt
    // Todo return user
    public Result register(Http.Request request) {
        try {
            Credentials registrationCredentials = httpHelper.getRegistrationData(request);
            ValidationHelper.validateRegistrationCredentials(registrationCredentials);
            User user = new User(registrationCredentials.username, registrationCredentials.password, UserRoles.REGULAR_USER);
            Session session = sessionService.createNewSession(user.id);
            userService.saveUser(user);
            return ok(session.getJwt());
        } catch (Exception e) {
            return badRequest(e.getMessage());
        }
    }
}
