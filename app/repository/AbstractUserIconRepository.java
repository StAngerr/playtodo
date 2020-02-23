package repository;

import models.UserIcon;

public abstract class AbstractUserIconRepository implements UserIconRepository {
    public static UserIconRepository getIconRepo(UserIcon icon) {
        if (icon.getProvider().equals("local")) {
            return new UserIconRepositoryImpl(icon);
        }
        return null;
    }
}
