package repository;

import com.google.inject.Inject;
import enums.UserRoles;
import models.User;
import play.db.Database;
import utils.collections.MyList;
import utils.errorHandler.ErrorReadingUserStorage;
import utils.errorHandler.UserAlreadyExist;

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
// how properly handle errors inside lamda
    @Override
    public User createUser(User user) throws UserAlreadyExist {
        return db.withConnection(connection -> {
            ResultSet findUser = connection.prepareStatement("SELECT * FROM users WHERE username=\"" + user.getUsername() + "\";").executeQuery();
            if (findUser.next()) {
                // throw new UserAlreadyExist();
                return null;
            } else {
                connection.prepareStatement("INSERT INTO users (username, password, role, age, email) " +
                        "VALUES (\"" + user.getUsername() + "\",\"" + user.getPassword() + "\",\""
                        + user.getRole() + "\",\"" + user.getAge() + "\",\"" + user.getEmail() + "\")").executeUpdate();
                try {
                    return getUserByUsername(user.getUsername());
                } catch (ErrorReadingUserStorage errorReadingUserStorage) {
                    return null;
                }
            }
        });
    }

    @Override
    public User deleteUser(String id) {
         return db.withConnection(connection -> {
            connection.prepareStatement("DELETE FROM users WHERE id=\"" + id + "\";").executeUpdate();
            return null;
        });
    }

    @Override
    public User updateUser(User user)  {
        return db.withConnection(connection -> {
            connection.prepareStatement("UPDATE users SET " +
                    "username=\"" + user.getUsername() +"\"," +
                    "age=\"" + user.getAge() + "\"," +
                    "email=\"" + user.getEmail() + "\"" +
                    " WHERE id=\"" + user.getId() + "\";").executeUpdate();
            try {
                return getUserById(user.getId());
            } catch (ErrorReadingUserStorage errorReadingUserStorage) {
                //errorReadingUserStorage.printStackTrace();
                return null;
            }
        });
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
