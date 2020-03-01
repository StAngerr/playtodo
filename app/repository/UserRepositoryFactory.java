package repository;

import models.User;
import play.db.Database;
import utils.collections.MyList;
import utils.errorHandler.ErrorReadingUserStorage;
import utils.errorHandler.UserAlreadyExist;
import utils.errorHandler.UserNotFound;

import javax.inject.Inject;

public class UserRepositoryFactory implements UserRepository {
    Database db;

    @Inject
    public UserRepositoryFactory(Database db) {
        this.db = db;
    }

    public UserRepository getUserRepository() {
        return new MySqlUserRepositoryImpl(db);
    }

    @Override
    public MyList<User> getAllUsers() throws ErrorReadingUserStorage {
        return getUserRepository().getAllUsers();
    }

    @Override
    public User getUserById(String id) throws ErrorReadingUserStorage {
        return getUserRepository().getUserById(id);
    }

    @Override
    public User getUserByUsername(String username) throws ErrorReadingUserStorage {
        return getUserRepository().getUserByUsername(username);
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
}
