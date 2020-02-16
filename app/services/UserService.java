package services;

import enums.UserRoles;
import models.Credentials;
import models.User;
import models.dto.UserDTO;
import repository.UserRepository;
import repository.UserRepositoryImpl;
import utils.JsonHelper;
import utils.collections.MyList;
import utils.errorHandler.*;

import java.util.List;

public class UserService {
    private static UserService instance;
    private UserRepositoryImpl repo = new UserRepositoryImpl();
//    @Inject
//    private UserRepository repo;

    private UserService() {}

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public User getAndValidateUser(Credentials loginData) throws UserNotFound, InvalidCredentials, ErrorReadingUserStorage {
        User user;
        user = getUser(loginData.username);
        if (user == null) {
            throw new UserNotFound();
        }
        if (!user.getPassword().equals(loginData.password)) {
            throw new InvalidCredentials();
        }
        return user;
    }

    public User createUser(User user) throws UserAlreadyExist {
        return repo.createUser(user);
    }

    public User getUser(String username) throws ErrorReadingUserStorage {
        return repo.getUserByUsername(username);
    }

    public User getUserById(String userId) throws ErrorReadingUserStorage {
       return repo.getUserById(userId);
    }

    public User updateUser(User user) throws UserNotFound, ErrorReadingUserStorage {
        return repo.updateUser(user);
    }

    public void checkIfUserExists(User user) throws UserAlreadyExist, ErrorReadingUserStorage {
        User userOptional = repo.getUserByUsername(user.getUsername());
        if (userOptional == null) {
            throw new UserAlreadyExist();
        }
    }

    public UserDTO userToUserDTO(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getAge(), user.getUsername(), user.getEmail());
    }

    public void validatePermissions(User user, List<UserRoles> roles) throws NoPermission {
        boolean result = roles.stream().anyMatch(role -> role == user.getRole());
        if (!result) {
            throw new NoPermission();
        }
    }

    public MyList<User> getAllUsers() throws ErrorReadingUserStorage {
        return repo.getAllUsers();
    }

    public String usersToJSON(MyList<User> users) {
        return JsonHelper.usersToJson(users).toJSONString();
    }
}
