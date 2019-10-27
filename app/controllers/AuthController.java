package controllers;

import com.google.inject.Inject;
import models.Credentials;
import models.Session;
import play.cache.AsyncCacheApi;
import play.mvc.*;
import services.SessionService;
import utils.HttpHelper;
import utils.JwtHelper;
import utils.ValidationHelper;

import static play.mvc.Results.*;

public class AuthController {
    private HttpHelper httpHelper;
    private JwtHelper jwtHelper;
    private SessionService sessionService;

    @Inject
    public AuthController(AsyncCacheApi cache) {
        this.httpHelper = HttpHelper.getInstance();
        this.jwtHelper = JwtHelper.getInstance();
        this.sessionService = SessionService.getInstance(cache);
    }

    public Result login(Http.Request request) {
        Credentials loginCredentials = httpHelper.getLoginData(request);
        try {
            ValidationHelper.validateLoginCredentials(loginCredentials);
        } catch (Exception e) {
            return badRequest(e.getMessage());
        }
        return ok("Test");
    }

    public Result register(Http.Request request) {
        Credentials registrationCredentials = httpHelper.getRegistrationData(request);
        try {
            ValidationHelper.validateRegistrationCredentials(registrationCredentials);
            Session session = sessionService.createNewSession(registrationCredentials);
            return ok(session.getJwt());
        } catch (Exception e) {
            return badRequest(e.getMessage());
        }
    }
}
