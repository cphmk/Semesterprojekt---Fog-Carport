package app.persistence;

import app.entities.CarportDesign;
import app.entities.Order;
import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class OrderMapper {
    public static ArrayList<Order> getOrders(ConnectionPool connectionPool) throws DatabaseException {
        String sql = "select * from orders";

        ArrayList<Order> orders = new ArrayList<>();

        try {
            Connection connection = connectionPool.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int order_id = rs.getInt("order_id");
                Date date = rs.getDate("date");
                String status = rs.getString("status");
                int user_id = rs.getInt("user_id");
                String comment = rs.getString("comment");
                int carport_id = rs.getInt("carport_id");

                orders.add(new Order(order_id, date, status, user_id, comment, carport_id));
            }

        } catch (SQLException e) {
            throw new DatabaseException("DB fejl", e.getMessage());
        }
        return orders;
    }

    public static void deleteOrder(int orderID, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "delete from orders where order_id = ?";

        try {
            Connection connection = connectionPool.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, orderID);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Fejl i opdatering af en task");
            }

        } catch (SQLException e) {
            throw new DatabaseException("DB fejl", e.getMessage());
        }
    }

    public static void addOrder(CarportDesign carportDesign, int user_id, int carport_id, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO orders (status, date, user_id, comment, carport_id) VALUES (?,?,?,?,?)";

        try {
            Connection connection = connectionPool.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, "Pending");
            ps.setDate(2, Date.valueOf(LocalDate.now()));
            ps.setInt(3, user_id);
            ps.setString(4, carportDesign.getComment());
            ps.setInt(5, carport_id);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Fejl i opdatering af en task");
            }

        } catch (SQLException e) {
            throw new DatabaseException("DB fejl", e.getMessage());
        }
    }
}
