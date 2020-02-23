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
import utils.UserIconHelper;
import utils.collections.MyList;
import utils.errorHandler.*;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static play.mvc.Results.badRequest;
import static play.mvc.Results.ok;

public class UsersController {
    private SessionService sessionService;
    private UserService userService;
    private HttpHelper httpHelper;
    private RequestValidationService requestValidationService;

    @Inject
    public UsersController (HttpHelper httpHelper, UserService userService, RequestValidationService requestValidationService, SessionService sessionService) {
        this.sessionService = sessionService;
        this.userService = userService;
        this.httpHelper = httpHelper;
        this.requestValidationService = requestValidationService;
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
            allUsers.removeIf(user -> user.getRole() == UserRoles.ADMIN);
            return ok(userService.usersToJSON(allUsers));
        } catch (Exception e) {
            return badRequest(e.getMessage());
        }
    }

    public Result createUser(Http.Request request) {
        try {
            requestValidationService.validateSessionAndUser(request, new ArrayList<UserRoles>() {{ add(UserRoles.ADMIN); }});
            User createdUser = httpHelper.getUserFromRequest(request);
            userService.createUser(createdUser);
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
        try {
            return ok(FileManager.fileToByteArray(session.getUser().getIcon().getPath()));
        } catch (FileDoesNotExist | ErrorWhileReadingFile e) {
            return  badRequest(e.getMessage());
        }
    }

    public Result saveUserIcon(Http.Request request) {
        Session session;
        try {
            session = this.requestValidationService.validateSessionAndUser(request);
        } catch (Exception e) {
            return badRequest(e.getMessage());
        }
        Http.MultipartFormData.FilePart<Files.TemporaryFile> picture = UserIconHelper.getIconFromRequest(request, "picture");
        try {
            UserIconHelper.validateIconFileName(picture.getFilename());
            String path = "/public/assets/" + UserIconHelper.generateUserIconFileName(picture.getFilename());
            File newFile = FileManager.createFile(path);
            User user = session.getUser();
            user.setIcon(new UserIcon(path));
            userService.updateUser(user);
            picture.getRef().moveFileTo(newFile, true);
            return ok("File uploaded");
        } catch (FileNameIsToLong | ErrorCreatingFile | UserNotFound | ErrorReadingUserStorage e) {
            return badRequest(e.getMessage());
        }
    }
}
