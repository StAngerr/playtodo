package repository;

import models.UserIcon;
import utils.errorHandler.ErrorCreatingFile;
import utils.errorHandler.ErrorWhileReadingFile;
import utils.errorHandler.FileDoesNotExist;

public interface UserIconRepository {
    public byte[] getUserIcon() throws FileDoesNotExist, ErrorWhileReadingFile;

    public UserIcon setUserIconForUser() throws ErrorCreatingFile;
}
