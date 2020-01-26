package controllers;

import com.google.inject.Inject;
import enums.UserRoles;

import models.Session;
import models.User;
import models.UserIcon;
import play.cache.AsyncCacheApi;
import play.libs.Files;
import play.mvc.Http;
import play.mvc.Result;
import services.RequestValidationService;
import services.SessionService;
import services.UserService;
import utils.FileManager;
import utils.HttpHelper;
import utils.JsonHelper;
import utils.collections.MyList;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.UUID;

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
            MyList<User> allUsers = userService.getAllUsers();
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

    public Result getUserIcon(Http.Request request) throws UnsupportedEncodingException {
        Session session;
        try {
            session = this.requestValidationService.validateSessionAndUser(request);
        } catch (Exception e) {
            return badRequest("User not found.");
        }
        if (session.getUser().getIcon() == null) {
            return badRequest("Image not found.");
        }
        String filPath = session.getUser().getIcon().getPath();
        return  ok(new java.io.File(filPath));
    }

    public Result saveUserIcon(Http.Request request) {
        Session session;
        try {
            session = this.requestValidationService.validateSessionAndUser(request);
        } catch (Exception e) {
            return badRequest(e.getMessage());
        }
        Http.MultipartFormData<Files.TemporaryFile> body = request.body().asMultipartFormData();
        Http.MultipartFormData.FilePart<Files.TemporaryFile> picture = body.getFile("picture");
        if (picture.getFilename().length() > 50) {
            return badRequest("File name is too long.");
        }
        String fileName = UUID.randomUUID().toString() + "_" + picture.getFilename();
        String path = "\\public\\assets\\" + fileName;
        File newFile = FileManager.createFile(path);
        User user = session.getUser();
        user.setIcon(new UserIcon(path));
        userService.updateUser(user);
        if (newFile != null) {
            picture.getRef().moveFileTo(newFile, true);
        }
        return ok("File uploaded");
    }
}
