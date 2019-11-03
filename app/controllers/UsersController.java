package controllers;

import enums.UserRoles;
import models.Session;
import models.User;
import play.cache.AsyncCacheApi;
import play.mvc.Http;
import play.mvc.Result;
import services.SessionService;
import services.UserService;
import utils.HttpHelper;
import utils.JsonHelper;

import javax.inject.Inject;

import java.util.ArrayList;

import static play.mvc.Results.badRequest;
import static play.mvc.Results.ok;

public class UsersController {
    private SessionService sessionService;
    private UserService userService;
    private HttpHelper httpHelper;

    @Inject
    public UsersController (AsyncCacheApi cache) {
        this.sessionService = SessionService.getInstance(cache);
        this.userService = UserService.getInstance();
        this.httpHelper = HttpHelper.getInstance();
    }

    public Result getUserById(Http.Request request, String id) {
        try {
            Session session = sessionService.validateSession(request);
            User user = userService.getUserById(session.getUserId());
            User userToFind = userService.getUserById(id);
            if (userToFind == null) {
                return ok("User not found.");
            }
           return ok(JsonHelper.toJsonObject(userService.userToUserDTO(userToFind)).toJSONString());
        } catch (Exception e) {
            return badRequest(e.getMessage());
        }
    }

    public Result getAllUsers(Http.Request request) {

        return ok("all users");
    }

    public Result createUser(Http.Request request) {
        try {
            Session session = sessionService.validateSession(request);
            User user = userService.getUserById(session.getUserId());
            userService.validatePermissions(user, new ArrayList<UserRoles>() {{ add(UserRoles.ADMIN); }}); // what is this syntax
            User createdUser = httpHelper.getUserFromRequest(request);
            /// move out  this check
            userService.isUserExists(createdUser);
            userService.saveUser(createdUser);
            return ok(createdUser.asJson().toJSONString());
        } catch (Exception e) {
            return badRequest(e.getMessage());
        }
    }

    public Result deleteUser() {
        return ok("delete");
    }
}
