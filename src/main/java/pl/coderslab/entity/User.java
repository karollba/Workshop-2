package pl.coderslab.entity;

public class User {
    private int id;
    private String userName;
    private String email;
    private String password;


    // id
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }


    // username
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }


    // email
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }


    // password
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }


}
