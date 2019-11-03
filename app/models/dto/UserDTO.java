package models.dto;

public class UserDTO {
    public  String id;
    public  String name;
    public  int age;
    public  String username;
    public  String email;

     public UserDTO(String id, String name, int age, String username, String email) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.username = username;
        this.email = email;
     }
}
