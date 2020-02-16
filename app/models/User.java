package models;

import enums.UserRoles;
import org.json.simple.JSONObject;

import java.util.UUID;

public class User {
    public String getId() {
        return id;
    }

    private  String id;

    public String getName() {
        return name;
    }

    private  String name;
    private  int age;

    public int getAge() {
        return age;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UserRoles getRole() {
        return role;
    }

    private  String username;
    private  String email;
    private  String password;
    private UserRoles role;
    private UserIcon icon;

    private User() {}

    public User(String username, String password, UserRoles role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.id = UUID.randomUUID().toString();
    }

    public User(String username, String password, String email, int age, UserRoles role, String name) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.age = age;
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }

    public JSONObject asJson() {
        JSONObject json = new JSONObject();
        json.put("username", this.username);
        json.put("password", this.password);
        json.put("age", this.age);
        json.put("id", this.id);
        json.put("name", this.name);
        json.put("email", this.email);
        json.put("role", this.role.name());
        if (this.icon != null) {
            json.put("icon", this.icon.asJson());
        }
        return json;
    }

    public UserIcon getIcon() {
        return icon;
    }

    public void setIcon(UserIcon icon) {
        this.icon = icon;
    }
}
