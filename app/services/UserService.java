package services;

import com.google.gson.Gson;
import enums.UserRoles;
import models.Credentials;
import models.User;
import models.dto.UserDTO;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import utils.collections.MyList;
import utils.errorHandler.InvalidCredentials;
import utils.errorHandler.NoPermission;
import utils.errorHandler.UserExist;
import utils.errorHandler.UserNotFound;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UserService {
    private static UserService instance;
    private final String filePath = System.getProperty("user.dir") + "\\data\\users.json";

    private UserService() {}

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public User getAndValidateUser(Credentials loginData) throws UserNotFound, InvalidCredentials {
        User user = null;
        try {
            user = getUser(loginData.username);
        } catch (IOException | ParseException e) {
            throw new UserNotFound();
        }
        if (user == null) {
            throw new UserNotFound();
        }

        if (!user.password.equals(loginData.password)) {
            throw new InvalidCredentials();
        }

        return user;
    }

    public void saveUser(User user) throws UserExist {
        try {
            isUserExists(user);
            MyList<User> allUsers = getAllUsers();
            allUsers.add(user);
            saveToFile(usersToJSON(allUsers));
        } catch (ParseException | IOException e) {
            throw new UserExist();
        }

    }

    public User getUser(User userData) throws IOException, ParseException {
        MyList<User> list = getAllUsers();
        Optional<User> userOptional = list.getIf((User user) -> user.username.equals(userData.username));
        return userOptional.orElse(null);
    }

    public User getUser(String username) throws IOException, ParseException {
        MyList<User> list = getAllUsers();
        Optional<User> userOptional = list.getIf((User user) -> user != null && user.username.equals(username));
        return userOptional.orElse(null);
    }

    public User getUserById(String userId) throws IOException, ParseException {
        MyList<User> list = getAllUsers();
        Optional<User> userOptional = list.getIf((User user) -> user != null && user.id.equals(userId));
        return userOptional.orElse(null);
    }

    public void isUserExists(User user) throws IOException, ParseException, UserExist {
        MyList<User> list = getAllUsers();
        Optional<User> userOptional = list.getIf((User u) -> u != null && u.username.equals(user.username));
        if (userOptional.isPresent()) {
            throw new UserExist();
        }
    }

    public UserDTO userToUserDTO(User user) {
        return new UserDTO(user.id, user.name, user.age, user.username, user.email);
    }

    /// As if this a good way of building methods. return boolean or throw Exception ???
    public void validatePermissions(User user, List<UserRoles> roles) throws NoPermission {
        boolean result = roles.stream().anyMatch(role -> role == user.role);
        if (!result) {
            throw new NoPermission();
        }
    }

    // move to  separate file
    private void saveToFile(JSONArray users) throws IOException {
        createIfNoFile();
        FileWriter file = null;
        file = new FileWriter(filePath);
        file.write(users.toJSONString());
        file.flush();
        file.close();
    }

    // move to  separate file
    private void createIfNoFile() throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
            saveToFile(new JSONArray());
        }
    }

    public MyList<User> getAllUsers() throws IOException, ParseException {
        createIfNoFile();
        JSONParser parser = new JSONParser();
        Gson gson = new Gson();
        JSONArray  usersJson = (JSONArray) parser.parse(new FileReader(filePath));
        MyList<User> users = new MyList<>();
        for (Object item : usersJson) {
            String asString = item.toString();
            User asUser = gson.fromJson(asString, User.class);
            users.add(asUser);
        }
        return users;
    }

    public JSONArray usersToJSON(MyList<User> users) {
        JSONArray result = new JSONArray();
        for (User user : users) {
            result.add(user.asJson());
        }
        return result;
    }
}
