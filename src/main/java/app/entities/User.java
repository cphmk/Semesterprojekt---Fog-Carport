package app.entities;

public class User {
    private int user_id;
    private String username;
    private String password;
    private int phone_number;
    private String email;
    private boolean role;

    public User(int user_id, String username, String password, boolean role) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User(int user_id, String username, int phone_number, String email) {
        this.user_id = user_id;
        this.username = username;
        this.phone_number = phone_number;
        this.email = email;
    }

    public int getPhone_number() {
        return phone_number;
    }

    public String getEmail() {
        return email;
    }

    public int getUserId() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean getRole() {
        return role;
    }


    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
