package repository;

import models.UserIcon;
import utils.errorHandler.ErrorCreatingFile;
import utils.errorHandler.ErrorWhileReadingFile;
import utils.errorHandler.FileDoesNotExist;

public class UserIconRepositoryFactory implements UserIconRepository {

    private UserIconRepository getRepoFor(UserIcon icon) {
        if (icon.getProvider().equals("local")) {
            return new UserIconRepositoryImpl();
        }
        throw new RuntimeException("IconRepository not found for UserIcon " + icon.asJson().toJSONString());
    }

    @Override
    public byte[] getUserIcon(UserIcon icon) throws FileDoesNotExist, ErrorWhileReadingFile {
        return getRepoFor(icon).getUserIcon(icon);
    }

    @Override
    public UserIcon setUserIconForUser(UserIcon icon) throws ErrorCreatingFile {
        return getRepoFor(icon).setUserIconForUser(icon);
    }
}
