package app.persistence;

import app.entities.CarportDesign;
import app.entities.Order;
import app.entities.Order_item;
import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderMapper {
    public static ArrayList<Order> getOrders(ConnectionPool connectionPool) throws DatabaseException {
        String sql = "select * from orders";

        ArrayList<Order> orders = new ArrayList<>();

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ResultSet rs = ps.executeQuery();

            if (!rs.isBeforeFirst()) {
                // No orders found, return empty list
                return orders;
            }

            while (rs.next()) {
                int order_id = rs.getInt("order_id");
                Date date = rs.getDate("date");
                String status = rs.getString("status");
                int user_id = rs.getInt("user_id");
                String comment = rs.getString("comment");
                int carport_id = rs.getInt("carport_id");
                double price = rs.getDouble("price");

                orders.add(new Order(order_id, date, status, user_id, comment, carport_id, price));
            }

        } catch (SQLException e) {
            throw new DatabaseException("DB fejl", e.getMessage());
        }
        return orders;
    }


    public static void addOrderItems(List<Order_item> orderItems, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO order_item(variant_id, order_id, quantity, description, price) VALUES (?,?,?,?,?)";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {

            for (Order_item orderItem : orderItems) {
                ps.setInt(1, orderItem.getVariant_id());
                ps.setInt(2, orderItem.getOrder_id());
                ps.setInt(3, orderItem.getQuantity());
                ps.setString(4, orderItem.getDescription());
                ps.setDouble(5, orderItem.getPrice());

                ps.addBatch();
            }

            int[] rowsAffected = ps.executeBatch();

            for (int rows : rowsAffected) {
                if (rows != 1) {
                    throw new DatabaseException("Fejl ved oprettelse af ny order item");
                }
            }
        } catch (SQLException e) {
            String msg = "Der er sket en fejl. Prøv igen";
            if (e.getMessage().startsWith("ERROR: duplicate key value ")) {
                msg = "Order item findes allerede.";
            }
            throw new DatabaseException(msg, e.getMessage());
        }
    }

    public static ArrayList<Order_item> getOrderItems(int order_id, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT * FROM order_item INNER JOIN variant USING(variant_id) INNER JOIN material USING(material_id) WHERE order_id = ?";

        ArrayList<Order_item> orderItems = new ArrayList<>();

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, order_id);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("name");
                    int length = rs.getInt("length");
                    int quantity = rs.getInt("quantity");
                    String unit = rs.getString("unit");
                    String description = rs.getString("description");

                    orderItems.add(new Order_item(name, length, quantity, unit, description));
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("DB fejl", e.getMessage());
        }
        return orderItems;
    }

    public static void deleteOrder(int orderID, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "delete from orders where order_id = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, orderID);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Fejl i opdatering af en task");
            }

        } catch (SQLException e) {
            throw new DatabaseException("DB fejl", e.getMessage());
        }
    }

    public static void deleteOrderItem(int orderID, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "delete from order_item where order_id = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, orderID);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("DB fejl", e.getMessage());
        }
    }


    public static int addOrder(CarportDesign carportDesign, int user_id, int carport_id, ConnectionPool connectionPool) throws DatabaseException {

        String sql = "INSERT INTO orders (status, date, user_id, comment, carport_id) VALUES (?,?,?,?,?)";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ) {
            ps.setString(1, "PendingFog");
            ps.setDate(2, Date.valueOf(LocalDate.now()));
            ps.setInt(3, user_id);
            ps.setString(4, carportDesign.getComment());
            ps.setInt(5, carport_id);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Fejl i opdatering af en task");
            }

            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new DatabaseException("Fejl ved oprettelse af nyt order - kunne ikke finde genereret id");
            }

        } catch (SQLException e) {
            throw new DatabaseException("DB fejl", e.getMessage());
        }
    }

    public static ArrayList<Order> getOrdersByUserID(int userID, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "select * from orders WHERE user_id = ?";

        ArrayList<Order> orders = new ArrayList<>();

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setInt(1, userID);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int order_id = rs.getInt("order_id");
                Date date = rs.getDate("date");
                String status = rs.getString("status");
                int user_id = rs.getInt("user_id");
                String comment = rs.getString("comment");
                int carport_id = rs.getInt("carport_id");
                double price = rs.getDouble("price");

                orders.add(new Order(order_id, date, status, user_id, comment, carport_id, price));
            }

        } catch (SQLException e) {
            throw new DatabaseException("DB fejl", e.getMessage());
        }
        return orders;
    }


    public static void updateOrderStatus(int order_id, String status, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "update orders SET status = ? WHERE order_id = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, status);
            ps.setInt(2, order_id);


            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Fejl ved opdatering af order status");
            }
        } catch (SQLException e) {
            String msg = "Der er sket en fejl. Prøv igen";
            throw new DatabaseException(msg, e.getMessage());
        }
    }

    public static void updateOrderPrice(int order_id, double price, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "update orders SET price = ? WHERE order_id = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setDouble(1, price);
            ps.setInt(2, order_id);


            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Fejl ved opdatering af order status");
            }
        } catch (SQLException e) {
            String msg = "Der er sket en fejl. Prøv igen";
            throw new DatabaseException(msg, e.getMessage());
        }
    }

}
