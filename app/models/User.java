package models;

import enums.UserRoles;
import net.minidev.json.JSONObject;

import java.util.UUID;

public class User {
    public  String id;
    public  String name;
    public  int age;
    public  String username;
    public  String email;
    public  String password;
    public UserRoles role;

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
        return json;
    }
}
