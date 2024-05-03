package app.persistence;

import app.entities.Order;
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
                int order_item_id = rs.getInt("order_item_id");

                orders.add(new Order(order_id, date, status, user_id, order_item_id));
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
}
