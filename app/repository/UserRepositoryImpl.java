package repository;

import com.google.gson.Gson;
import models.User;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import utils.FileManager;
import utils.collections.MyList;
import utils.errorHandler.ErrorReadingUserStorage;
import utils.errorHandler.UserAlreadyExist;
import utils.errorHandler.UserNotFound;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class UserRepositoryImpl implements UserRepository {
    private final String filePath = System.getProperty("user.dir") + File.separator + "data" + File.separator + "users.json";

    @Override
    public MyList<User> getAllUsers() throws ErrorReadingUserStorage {
        Gson gson = new Gson();
        JSONArray usersJson = getAllUsersAsJsonArray();
        MyList<User> users = new MyList<>();
        for (Object item : usersJson) {
            User user = gson.fromJson(item.toString(), User.class);
            if (user != null) {
                users.add(user);
            }
        }
        return users;
    }


    @Override
    public User getUserById(String id) throws ErrorReadingUserStorage {
        return getAllUsers()
                .getIf((user) -> user.getId().equals(id))
                .orElse(null);
    }

    @Override
    public User getUserByUsername(String username) throws ErrorReadingUserStorage {
        return getAllUsers()
                .getIf((user) -> user.getUsername().equals(username))
                .orElse(null);
    }

    @Override
    public User createUser(User user) throws UserAlreadyExist {
        try {
            MyList<User> allUsers = getAllUsers();
            allUsers.add(user);
            saveUsers(allUsers);
        } catch (ErrorReadingUserStorage e) {
            throw new UserAlreadyExist();
        }
        return null;
    }

    @Override
    public User deleteUser(String id) {
        return null;
    }

    @Override
    public User updateUser(User user) throws ErrorReadingUserStorage, UserNotFound {
        MyList<User> allUsers = getAllUsers();
        User userToUpdate = allUsers.find((u) -> u.getId().equals(user.getId()));
        if (userToUpdate != null) {
            allUsers.updateByIndex(allUsers.indexOf(userToUpdate), user);
            saveUsers(allUsers);
        } else {
            throw new UserNotFound();
        }
        return user;
    }

    private void saveUsers(Collection<User> users) throws ErrorReadingUserStorage {
        try {
            Gson gson = new Gson();
            FileManager.saveToFile(filePath, gson.toJson(users));
        } catch (IOException e) {
            throw new ErrorReadingUserStorage(e);
        }
    }

    private JSONArray getAllUsersAsJsonArray() throws ErrorReadingUserStorage {
        try {
            JSONParser parser = new JSONParser();
            return (JSONArray) parser.parse(FileManager.readFile(filePath));
        } catch (IOException | ParseException e) {
            throw new ErrorReadingUserStorage(e);
        }
    }
}
