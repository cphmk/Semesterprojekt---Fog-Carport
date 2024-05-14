package app.controllers;

import app.entities.CarportDesign;
import app.persistence.ConnectionPool;
import app.services.CarportSvg;
import app.services.Svg;
import app.entities.Order;
import app.entities.Order_item;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import app.services.Calculator;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class OrderController
{
    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/showOrder", ctx -> showOrder(ctx));
    }

    public static void showOrder(Context ctx)
    {
        // TODO: Create a SVG Drawing and inject into the showOrder.html template as a String
        Locale.setDefault(new Locale("US"));
        CarportDesign carportDesign = new CarportDesign(600,780, "", 530,390, "");
        CarportSvg svg = new CarportSvg(carportDesign);
    }

  
    private static void sendRequest(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        CarportDesign carportDesign = ctx.sessionAttribute("Carport");
        int order_id = ctx.sessionAttribute("order_id");

        // Calculate order items using Calculator
        Calculator calculator = new Calculator(carportDesign, order_id, connectionPool);
        List<Order_item> orderItems = calculator.getOrderItems();

        // Save order items in the database
        try (Connection connection = connectionPool.getConnection()) {
            for (Order_item orderItem : orderItems) {
                // Prepare SQL statement to insert order item
                String sql = "INSERT INTO order_items (order_id, quantity, description, material_id, price) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setInt(1, order_id);
                    statement.setInt(2, orderItem.getQuantity());
                    statement.setString(3, orderItem.getDescription());
                    statement.setInt(4, orderItem.getMaterial_id());
                    statement.setDouble(5, orderItem.getPrice());

                    // Execute the insert statement
                    statement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            // Handle database errors
            throw new DatabaseException("Error saving order items to database", e.getMessage());
        }

        // Render the response to the user
        ctx.render("public/templates/showOrder.html");
    }

