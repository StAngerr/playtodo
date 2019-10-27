package utils;

import models.Credentials;

public class ValidationHelper {

    public static void validateLoginCredentials(Credentials creds) throws Exception {
        validateUsername(creds.username);
        validatePassword(creds.password);
    }

    public static void validateRegistrationCredentials(Credentials creds) throws Exception {
        validateUsername(creds.username);
        validatePassword(creds.password);
        validatePassword(creds.confirmPassword);
        comparePassword(creds.password, creds.confirmPassword);
    }

    private static void validateUsername(String username) throws Exception {
        boolean noUsername = username == null || username.trim().isEmpty();
        if (noUsername) {
            throw new Exception("No credentials provided.");
        }
        if (username.length() > 50) {
            throw new Exception("Invalid credentials.");
        }
    }

    private static void validatePassword(String password) throws Exception {
        boolean noPassword = password == null || password.isEmpty();
        if (noPassword) {
            throw new Exception("No credentials provided.");
        }
        if (password.length() > 50) {
            throw new Exception("Invalid credentials.");
        }
    }

    private static void comparePassword(String password, String confirmPassword) throws Exception {
        if (!password.equals(confirmPassword)) {
            throw new Exception("Passwords didn't match");
        }
    }
}
