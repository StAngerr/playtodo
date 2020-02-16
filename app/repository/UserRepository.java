package repository;

import models.User;
import utils.collections.MyList;
import utils.errorHandler.ErrorReadingUserStorage;
import utils.errorHandler.UserAlreadyExist;
import utils.errorHandler.UserNotFound;

public interface UserRepository {
    public MyList<User> getAllUsers() throws ErrorReadingUserStorage;

    public User getUserById(String id) throws ErrorReadingUserStorage;

    public User getUserByUsername(String username) throws ErrorReadingUserStorage;

    public User createUser(User user) throws UserAlreadyExist;

    public User deleteUser(String id);

    public User updateUser(User user) throws ErrorReadingUserStorage, UserNotFound;

}
