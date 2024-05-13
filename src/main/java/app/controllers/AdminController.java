package app.controllers;

import app.entities.Order;
import app.entities.Order_item;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.ArrayList;

public class AdminController {
    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("adminView", ctx -> getAllOrders(ctx, connectionPool));
        app.post("adminView", ctx -> getAllOrders(ctx, connectionPool));
        app.post("deleteOrderAdmin", ctx -> deleteOrder(ctx, connectionPool));
        app.get("viewUsers", ctx -> getAllUsers(ctx, connectionPool));
    }

    public static void getAllOrders(Context ctx, ConnectionPool connectionPool) {
        ArrayList<Order> orders = new ArrayList<>();
        try {
            orders = OrderMapper.getOrders(connectionPool);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }

        ctx.sessionAttribute("orders", orders);
        ctx.render("adminOrders.html");
    }

    public static void deleteOrder(Context ctx, ConnectionPool connectionPool) {
        int orderID = Integer.parseInt(ctx.formParam("orderID"));

        try {
            OrderMapper.deleteOrder(orderID, connectionPool);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
        ctx.redirect("admin");
    }

    public static void getAllUsers(Context ctx, ConnectionPool connectionPool) {
        ArrayList<User> users = new ArrayList<>();
        try {
            users = UserMapper.getAllUsers(connectionPool);

        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
        ctx.sessionAttribute("users", users);
        ctx.render("adminUsers.html");
    }
}
