package models;

public class Credentials {
    public String username;
    public String password;
    public String confirmPassword;

    public Credentials() {}

    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Credentials(String username, String password, String confirmPassword) {
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    @Override
    public String toString() {
        return "Username: " + this.username + " Password: " + this.password;
    }
}
