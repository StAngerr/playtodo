package repository;

import com.google.inject.Inject;
import enums.UserRoles;
import models.User;
import play.db.Database;
import utils.collections.MyList;
import utils.errorHandler.ErrorReadingUserStorage;
import utils.errorHandler.UserAlreadyExist;
import utils.errorHandler.UserNotFound;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MySqlUserRepositoryImpl implements UserRepository {
    private Database db;

    @Inject
    public MySqlUserRepositoryImpl(Database db) {
        this.db = db;
    }

    @Override
    public MyList<User> getAllUsers() throws ErrorReadingUserStorage {
        return db.withConnection(
                connection -> {
                    ResultSet data = connection.prepareStatement("SELECT * FROM users;").executeQuery();
                    MyList<User> users = new MyList<>();
                    while (data.next()) {
                        users.add(new User(
                                data.getString("id"),
                                data.getString("username"),
                                data.getString("password"),
                                UserRoles.valueOf(data.getString("role"))
                        ));
                    }
                    return users;
                });
    }

    @Override
    public User getUserById(String id) throws ErrorReadingUserStorage {
        return db.withConnection(connection -> {
            ResultSet data = connection.prepareStatement("SELECT * FROM users WHERE id=\"" + id + "\";").executeQuery();
            return data.next() ? new User(
                    data.getString("id"),
                    data.getString("username"),
                    data.getString("password"),
                    UserRoles.valueOf(data.getString("role"))
            ) : null;
        });
    }

    @Override
    public User getUserByUsername(String username) throws ErrorReadingUserStorage {
        return db.withConnection(connection -> {
            ResultSet data = connection.prepareStatement("SELECT * FROM users WHERE username=\"" + username + "\";").executeQuery();
            return data.next() ? new User(
                    data.getString("id"),
                    data.getString("username"),
                    data.getString("password"),
                    UserRoles.valueOf(data.getString("role"))
            ) : null;
        });
    }

    @Override
    public User createUser(User user) throws UserAlreadyExist {
        return null;
    }

    @Override
    public User deleteUser(String id) {
        return null;
    }

    @Override
    public User updateUser(User user) throws ErrorReadingUserStorage, UserNotFound {
        return null;
    }

    private ArrayList<User> resultToJson(ResultSet data) throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        while (data.next()) {
            users.add(new User(
                    data.getString("id"),
                    data.getString("username"),
                    data.getString("password"),
                    UserRoles.valueOf(data.getString("role"))
            ));
        }
        return users;
    }
}
