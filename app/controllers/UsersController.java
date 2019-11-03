package controllers;

import com.google.inject.Inject;
import enums.UserRoles;

import models.User;
import play.cache.AsyncCacheApi;
import play.mvc.Http;
import play.mvc.Result;
import services.RequestValidationService;
import services.SessionService;
import services.UserService;
import utils.HttpHelper;
import utils.JsonHelper;

import java.util.ArrayList;
import java.util.List;

import static play.mvc.Results.badRequest;
import static play.mvc.Results.ok;

public class UsersController {
    private SessionService sessionService;
    private UserService userService;
    private HttpHelper httpHelper;
    private RequestValidationService requestValidationService;

    @Inject
    public UsersController (AsyncCacheApi cache) {
        this.sessionService = SessionService.getInstance(cache);
        this.userService = UserService.getInstance();
        this.httpHelper = HttpHelper.getInstance();
        this.requestValidationService = RequestValidationService.getInstance(cache);
    }

    public Result getUserById(Http.Request request, String id) {
        try {
            this.requestValidationService.validateSessionAndUser(request);
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
        try {
            this.requestValidationService.validateSessionAndUser(request);
            List<User> allUsers = userService.getAllUsers();
            allUsers.removeIf(user -> user.role == UserRoles.ADMIN);
            return ok(userService.usersToJSON(allUsers).toJSONString());
        } catch (Exception e) {
            return badRequest(e.getMessage());
        }
    }

    public Result createUser(Http.Request request) {
        try {
            requestValidationService.validateSessionAndUser(request, new ArrayList<UserRoles>() {{ add(UserRoles.ADMIN); }});
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
