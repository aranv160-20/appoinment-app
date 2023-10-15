package Package_BC3;

public class User {
    int id;
    String username;
    String password;
    String role;
    String realID;
    User(int id, String username, String password, String role, String realID){
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.realID = realID;
    }
}
