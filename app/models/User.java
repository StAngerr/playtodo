package models;

import net.minidev.json.JSONObject;

import java.util.UUID;

public class User {
    public  String id;
    public  String name;
    public  int age;
    public  String username;
    public  String email;
    public  String password;

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.id = UUID.randomUUID().toString();
    }

    public JSONObject asJson() {
        JSONObject json = new JSONObject();
        json.put("username", this.username);
        json.put("password", this.password);
        json.put("age", this.age);
        json.put("id", this.id);
        json.put("name", this.name);
        json.put("email", this.email);
        return json;
    }
}
