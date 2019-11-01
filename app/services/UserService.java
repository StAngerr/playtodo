package services;

import com.google.gson.Gson;
import models.Credentials;
import models.User;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private static UserService instance;

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
            List<User> list = getAllUsers();
            for (User item : list) {
                if (item.username.equals(user.username)) {
                    throw new Exception("User exist.");
                }
            }
            list.add(user);
            saveToFile(usersToJSON(list));
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

    private void saveToFile(JSONArray users) throws IOException {
        FileWriter file = null;
        file = new FileWriter(System.getProperty("user.dir") + "\\app\\data\\users.json");
        file.write(users.toJSONString());
        file.flush();
        file.close();
    }

    private List<User> getAllUsers() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Gson gson = new Gson();
        JSONArray  usersJson = (JSONArray) parser.parse(new FileReader(System.getProperty("user.dir") + "\\app\\data\\users.json"));
        List<User> users = new ArrayList<>();
        for (Object item : usersJson) {
            users.add(gson.fromJson(item.toString(), User.class));
        }
        return users;
    }

    private JSONArray usersToJSON(List<User> users) {
        JSONArray result = new JSONArray();
        for (User user : users) {
            result.add(user.asJson());
        }
        return result;
    }
}
