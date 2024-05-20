package app.controllers;

import app.entities.CarportDesign;
import app.entities.Order;
import app.entities.Order_item;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.CarportMapper;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import app.persistence.UserMapper;
import app.services.CarportSvg;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.Locale;

public class AdminController {
    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("adminView", ctx -> getAllOrders(ctx, connectionPool));
        app.post("adminView", ctx -> getAllOrders(ctx, connectionPool));
        app.post("deleteOrderAdmin", ctx -> deleteOrder(ctx, connectionPool));
        app.get("viewUsers", ctx -> getAllUsers(ctx, connectionPool));
        app.post("viewOrder", ctx -> viewOrder(ctx, connectionPool));
        app.post("adminApprove", ctx -> approve(ctx,connectionPool));
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
            OrderMapper.deleteOrderItem(orderID, connectionPool);
            OrderMapper.deleteOrder(orderID, connectionPool);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
        ctx.redirect("adminView");
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

    public static void viewOrder(Context ctx, ConnectionPool connectionPool) {
        int orderID = Integer.parseInt(ctx.formParam("order_id"));

        Locale.setDefault(new Locale("US"));

        //Get carport design from db using order id
        CarportDesign carportDesign = null;
        try {
            carportDesign = CarportMapper.getCarportDesign(orderID,connectionPool);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }

        //Make svg string and pass it through attribute onto showOrder.html
        CarportSvg svg = new CarportSvg(carportDesign);
        ctx.attribute("svg",svg.toString());

        //Get length from variant using id, and name and unit from material.
        try {
            ArrayList<Order_item> stykliste = OrderMapper.getOrderItems(orderID,connectionPool);
            ctx.sessionAttribute("stykliste",stykliste);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }

        //Save orderID to the next page, so it knows which order to approve.
        ctx.attribute("order_id",orderID);

        // TODO: save the order status, so it can be used in showOrder.html
        Order order = null;
        try {
            order = OrderMapper.getOrderByID(orderID,connectionPool);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
        ctx.attribute("order", order);

        ctx.render("showOrder.html");
    }

    public static void approve(Context ctx, ConnectionPool connectionPool) {
        int orderID = Integer.parseInt(ctx.formParam("order_id"));

        try {
            OrderMapper.updateOrderStatus(orderID,"PendingUser",connectionPool);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }

        ctx.redirect("adminView");

    }
}
