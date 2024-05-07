package app.entities;

public class User {
    private int userId;
    private String username;
    private String password;
    private boolean role;
    private String address;
    public User(int userId, String userName, String password, boolean role, String address) {
        this.userId = userId;
        this.username = userName;
        this.password = password;
        this.role = role;
        this.address = address;

    }

    public int getUserId() {
        return userId;
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

    public String getAddress() {
       return address;
    }
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
