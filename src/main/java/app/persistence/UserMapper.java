package app.persistence;

import app.entities.ContactInformation;
import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;

public class UserMapper {
    public static User login(String username, String password, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "select * from users where username=? and password=?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int user_id = rs.getInt("user_id");
                boolean role = rs.getBoolean("admin");
                System.out.println("Sign in success");
                return new User(user_id, username, password, role);
            } else {
                throw new DatabaseException("Fejl i login. Prøv igen");
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB fejl", e.getMessage());
        }
    }

    public static void createuser(String username, String password, String address, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "insert into users (username, password, address) values (?,?,?)";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, address);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Fejl ved oprettelse af ny bruger");
            }
        } catch (SQLException e) {
            String msg = "Der er sket en fejl. Prøv igen";
            if (e.getMessage().startsWith("ERROR: duplicate key value ")) {
                msg = "Brugernavnet findes allerede. Vælg et andet";
            }
            throw new DatabaseException(msg, e.getMessage());
        }
    }

    public static ContactInformation getContactInformation(int userId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT * FROM contact WHERE user_id = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String address = rs.getString("address");
                String city = rs.getString("city");
                int postal_code = rs.getInt("postal_code");
                int phone_number = rs.getInt("phone_number");
                String email = rs.getString("email");
                System.out.println("Contact information gathered");

                return new ContactInformation(name,address,postal_code,city,phone_number,email);
            } else {
                throw new DatabaseException("Fejl i login. Prøv igen");
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB fejl", e.getMessage());
        }
    }


    public static void insertContactInformation(User user, ConnectionPool connectionPool) throws DatabaseException {
        //Insert into contact table with information
        String contactSQL = "INSERT INTO contact (name,address,city,postal_code,phone_number,email) VALUES (?,?,?,?,?,?) WHERE user_id = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(contactSQL)
        ) {
            ps.setString(1, user.getContactInformation().getName());
            ps.setString(2, user.getContactInformation().getAddress());
            ps.setString(3, user.getContactInformation().getCity());
            ps.setInt(4, user.getContactInformation().getPostal_code());
            ps.setInt(5, user.getContactInformation().getPhone_number());
            ps.setString(6, user.getContactInformation().getEmail());

            ps.setInt(7,user.getUser_id());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Fejl ved oprettelse af ny kontakt information");
            }
        } catch (SQLException e) {
            String msg = "Der er sket en fejl. Prøv igen";
            if (e.getMessage().startsWith("ERROR: duplicate key value ")) {
                msg = "Kontaktinformationen findes allerede.";
            }
            throw new DatabaseException(msg, e.getMessage());
        }
    }

    public static void updateContactInformation(User user, ConnectionPool connectionPool) throws DatabaseException {
        String contactSQL = "UPDATE contact SET name = ?, address = ?, city = ?, postal_code = ?, phone_number = ?, email = ? WHERE user_id = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(contactSQL)
        ) {
            ps.setString(1, user.getContactInformation().getName());
            ps.setString(2, user.getContactInformation().getAddress());
            ps.setString(3, user.getContactInformation().getCity());
            ps.setInt(4, user.getContactInformation().getPostal_code());
            ps.setInt(5, user.getContactInformation().getPhone_number());
            ps.setString(6, user.getContactInformation().getEmail());

            ps.setInt(7,user.getUser_id());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Fejl ved opdatering af ny kontakt information");
            }
        } catch (SQLException e) {
            String msg = "Der er sket en fejl. Prøv igen";
            throw new DatabaseException(msg, e.getMessage());
        }
    }

    public static ArrayList<User> getAllUsers(ConnectionPool connectionPool) throws DatabaseException {
        ArrayList<User> user = new ArrayList<>();
        String sql = "SELECT * FROM users INNER JOIN contact ON users.user_id = contact.user_id WHERE users.admin = false";
        try {
            Connection connection = connectionPool.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int user_id = rs.getInt("user_id");
                String username = rs.getString("username");

                String name = rs.getString("name");
                String address = rs.getString("address");
                String city = rs.getString("city");
                int postal_code = rs.getInt("postal_code");
                int phone_number = rs.getInt("phone_number");
                String email = rs.getString("email");

                ContactInformation contactInformation = new ContactInformation(name,address,postal_code,city,phone_number,email);
                user.add(new User(user_id, username, contactInformation));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

}