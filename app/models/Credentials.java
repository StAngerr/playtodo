package models;

public class Credentials {

    private String username;
    private String password;
    private String confirmPassword;

    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Credentials(String username, String password, String confirmPassword) {
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    @Override
    public String toString() {
        return "Username: " + this.username + " Password: " + this.password;
    }
}
