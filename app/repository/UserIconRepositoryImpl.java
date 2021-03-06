package repository;

import models.UserIcon;
import utils.FileManager;
import utils.UserIconHelper;
import utils.errorHandler.ErrorCreatingFile;
import utils.errorHandler.ErrorWhileReadingFile;
import utils.errorHandler.FileDoesNotExist;

import java.io.File;

class UserIconRepositoryImpl implements UserIconRepository {

    @Override
    public byte[] getUserIcon(UserIcon icon) throws FileDoesNotExist, ErrorWhileReadingFile {
        return FileManager.fileToByteArray(icon.getPath());
    }

    @Override
    public UserIcon setUserIconForUser(UserIcon icon) throws ErrorCreatingFile {
        String path = "/public/assets/" + UserIconHelper.generateUserIconFileName(icon.getFileName());
        File newFile = FileManager.createFile(path);
        icon.setPath(path);
        icon.getRef().moveFileTo(newFile, true);
        return icon;
    }
}
