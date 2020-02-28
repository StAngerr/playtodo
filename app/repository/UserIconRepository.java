package repository;

import models.UserIcon;
import utils.errorHandler.ErrorCreatingFile;
import utils.errorHandler.ErrorWhileReadingFile;
import utils.errorHandler.FileDoesNotExist;

public interface UserIconRepository {
    public byte[] getUserIcon(UserIcon icon) throws FileDoesNotExist, ErrorWhileReadingFile;

    public UserIcon setUserIconForUser(UserIcon icon) throws ErrorCreatingFile;
}
