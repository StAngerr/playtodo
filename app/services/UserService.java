package services;

import com.google.gson.Gson;
import enums.UserRoles;
import models.Credentials;
import models.User;
import models.dto.UserDTO;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public User getAndValidateUser(Credentials loginData) throws IOException, ParseException {
        User user = getUser(loginData.username);
        if (user == null) {
            return null;
        }

        if (!user.password.equals(loginData.password)) {
            return null;
        }

        return user;
    }

    public void saveUser(User user) throws Exception {
        try {
            isUserExists(user);
            List<User> allUsers = getAllUsers();
            allUsers.add(user);
            saveToFile(usersToJSON(allUsers));
        } catch (IOException e) {
            System.out.println(e);
            throw new Exception("Failed to create user.");
        }
    }

    public User getUser(User user) throws IOException, ParseException {
        List<User> list = getAllUsers();
        for (User item : list) {
            if (item.username.equals(user.username)) {
                return item;
            }
        }
        return null;
    }

    public User getUser(String username) throws IOException, ParseException {
        List<User> list = getAllUsers();
        for (User item : list) {
            if (item.username.equals(username)) {
                return item;
            }
        }
        return null;
    }

    public User getUserById(String userId) throws IOException, ParseException {
        List<User> list = getAllUsers();
        for (User item : list) {
            if (item.id.equals(userId)) {
                return item;
            }
        }
        return null;
    }

    public void isUserExists(User user) throws Exception {
        List<User> list = getAllUsers();
        for (User item : list) {
            if (item.username.equals(user.username)) {
                throw new Exception("User exist.");
            }
        }
    }

    public UserDTO userToUserDTO(User user) {
        return new UserDTO(user.id, user.name, user.age, user.username, user.email);
    }

    /// As if this a good way of building methods. return boolean or throw Exception ???
    public void validatePermissions(User user, List<UserRoles> roles) throws Exception {
        boolean result = roles.stream().anyMatch(role -> role == user.role);
        if (!result) {
            throw new Exception("User have no permissions for this action.");
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

    public List<User> getAllUsers() throws IOException, ParseException {
        createIfNoFile();
        JSONParser parser = new JSONParser();
        Gson gson = new Gson();
        JSONArray  usersJson = (JSONArray) parser.parse(new FileReader(filePath));
        List<User> users = new ArrayList<>();
        for (Object item : usersJson) {
            users.add(gson.fromJson(item.toString(), User.class));
        }
        return users;
    }

    public JSONArray usersToJSON(List<User> users) {
        JSONArray result = new JSONArray();
        for (User user : users) {
            result.add(user.asJson());
        }
        return result;
    }
}
