package controllers;

import com.google.inject.Inject;
import enums.UserRoles;

import models.Session;
import models.User;
import models.UserIcon;
import play.libs.Files;
import play.mvc.Http;
import play.mvc.Result;
import repository.UserIconRepository;
import services.RequestValidationService;
import services.UserService;
import utils.HttpHelper;
import utils.JsonHelper;
import utils.UserIconHelper;
import utils.collections.MyList;
import utils.errorHandler.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static play.mvc.Results.badRequest;
import static play.mvc.Results.ok;

public class UsersController {
    private UserService userService;
    private HttpHelper httpHelper;
    private RequestValidationService requestValidationService;
    private final UserIconRepository userIconRepository;

    @Inject
    public UsersController (HttpHelper httpHelper, UserService userService, RequestValidationService requestValidationService, UserIconRepository userIconRepository) {
        this.userService = userService;
        this.httpHelper = httpHelper;
        this.requestValidationService = requestValidationService;
        this.userIconRepository = userIconRepository;
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
            return ok(userIconRepository.getUserIcon(session.getUser().getIcon()));
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

        try {
            Http.MultipartFormData.FilePart<Files.TemporaryFile> picture = UserIconHelper.getIconFromRequest(request, "picture");
            UserIconHelper.validateIconFileName(picture.getFilename());
            UserIcon icon = userIconRepository.setUserIconForUser(new UserIcon(null, picture.getFilename(), picture.getRef()));
            User user = session.getUser();
            user.setIcon(icon);
            userService.updateUser(user);
            return ok("File uploaded");
        } catch (FileNameIsToLong | ErrorCreatingFile | UserNotFound | ErrorReadingUserStorage e) {
            return badRequest(e.getMessage());
        }
    }
}
