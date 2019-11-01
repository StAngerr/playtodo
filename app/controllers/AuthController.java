package controllers;

import com.google.inject.Inject;
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

    public Result login(Http.Request request) {
        Credentials loginCredentials = httpHelper.getLoginData(request);
        System.out.println(loginCredentials.toString());
        try {
            ValidationHelper.validateLoginCredentials(loginCredentials);
            User user = userService.getAndValidateUser(loginCredentials);
            Session session = sessionService.createNewSession();
            session.setUser(user);
            return ok(session.asJson().toJSONString());
        } catch (Exception e) {
            return badRequest(e.getMessage());
        }
    }

    public Result register(Http.Request request) {
        Credentials registrationCredentials = httpHelper.getRegistrationData(request);
        try {
            ValidationHelper.validateRegistrationCredentials(registrationCredentials);
            Session session = sessionService.createNewSession();
            userService.saveUser(new User(registrationCredentials.username, registrationCredentials.password));
            return ok(session.getJwt());
        } catch (Exception e) {
            return badRequest(e.getMessage());
        }
    }
}
