package app.entities;

public class User {
    private int user_id;
    private String username;
    private String password;
    private boolean role;
    private ContactInformation contactInformation;

    public User(int user_id, String username, String password, boolean role) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User(int user_id, String username, ContactInformation contactInformation) {
        this.user_id = user_id;
        this.username = username;
        this.contactInformation = contactInformation;
    }

    public int getUser_id() {
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


    public ContactInformation getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(ContactInformation contactInformation) {
        this.contactInformation = contactInformation;
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
