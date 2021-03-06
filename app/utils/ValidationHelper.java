package utils;

import models.Credentials;
import utils.errorHandler.InvalidCredentials;
import utils.errorHandler.NoCredentials;
import utils.errorHandler.PasswordMatch;

public class ValidationHelper {

    public static void validateLoginCredentials(Credentials creds) throws InvalidCredentials, NoCredentials {
        validateUsername(creds.getUsername());
        validatePassword(creds.getPassword());
    }

    public static void validateRegistrationCredentials(Credentials creds) throws InvalidCredentials, NoCredentials, PasswordMatch {
        validateUsername(creds.getUsername());
        validatePassword(creds.getPassword());
        validatePassword(creds.getConfirmPassword());
        comparePassword(creds.getPassword(), creds.getConfirmPassword());
    }

    private static void validateUsername(String username) throws InvalidCredentials, NoCredentials {
        boolean noUsername = username == null || username.trim().isEmpty();
        if (noUsername) {
            throw new NoCredentials();
        }
        if (username.length() > 50) {
            throw new InvalidCredentials();
        }
    }

    private static void validatePassword(String password) throws InvalidCredentials, NoCredentials {
        boolean noPassword = password == null || password.isEmpty();
        if (noPassword) {
            throw new NoCredentials();
        }
        if (password.length() > 50) {
            throw new InvalidCredentials();
        }
    }

    private static void comparePassword(String password, String confirmPassword) throws PasswordMatch {
        if (!password.equals(confirmPassword)) {
            throw new PasswordMatch();
        }
    }
}
