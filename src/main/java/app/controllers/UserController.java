package app.controllers;


import app.entities.ContactInformation;
import app.entities.Order;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.ArrayList;

public class UserController {
    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.post("login", ctx -> login(ctx, connectionPool));
        app.get("login", ctx -> {
            String redirectUrl = ctx.req().getHeader("Referer");
            ctx.sessionAttribute("redirectUrl", redirectUrl);
            ctx.render("loginpage.html");
        });
        app.post("loginpage", ctx -> {
            String redirectUrl = ctx.req().getHeader("Referer");
            ctx.sessionAttribute("redirectUrl", redirectUrl);
            ctx.render("loginpage.html");
        });
        app.get("logout", ctx -> logout(ctx));
        app.get("signup", ctx -> ctx.render("signup.html"));
        app.post("signup", ctx -> signup(ctx, connectionPool));
        app.get("viewAccount", ctx -> getOrder(ctx, connectionPool));
        app.post("viewAccount", ctx -> getOrder(ctx, connectionPool));
        app.post("deleteOrder", ctx -> deleteOrder(ctx, connectionPool));
        app.post("acceptOffer", ctx -> acceptOffer(ctx, connectionPool));
    }

    private static void logout(Context ctx) {
        ctx.req().getSession().invalidate();
        ctx.redirect("/");
    }

    private static void signup(Context ctx, ConnectionPool connectionPool) {
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");
        String cpassword = ctx.formParam("cpassword");

        if (cpassword.equals(password)) {
            try {
                UserMapper.createuser(username, password, connectionPool);
                ctx.render("loginpage.html");
            } catch (DatabaseException e) {
                ctx.attribute("message", "Username already exists");
                ctx.render("signup.html");
            }
        } else {
            ctx.attribute("message", "Passwords do not match!");
            ctx.render("signup.html");
        }
    }


    public static void login(Context ctx, ConnectionPool connectionPool) {
        // Hent form parametre
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");


        // Check om bruger findes i DB med de angivne username + password
        try {
            User user = UserMapper.login(username, password, connectionPool);
            ContactInformation contactInformation = UserMapper.getContactInformation(user.getUser_id(), connectionPool);
            if (contactInformation != null) {
                user.setContactInformation(contactInformation);
            }
            ctx.sessionAttribute("currentUser", user);
            if (user.getRole()) {
                ctx.sessionAttribute("loggedIn", true);
                ctx.redirect("adminView");
            } else {
                ctx.sessionAttribute("loggedIn", true);
                String previousPage = ctx.sessionAttribute("redirectUrl");
                ctx.redirect(previousPage != null ? previousPage : "/");
            }
        } catch (DatabaseException e) {
            // Hvis nej, send tilbage til login side med fejl besked
            if (e.getMessage().equals("DB fejl")) {
                ctx.attribute("message", "Can't connect to db");
            }
            if (e.getMessage().equals("Fejl i login. Prøv igen")) {
                ctx.attribute("message", "Wrong username or password");
            }
            ctx.render("loginpage.html");
        }

    }

    public static void getOrder(Context ctx, ConnectionPool connectionPool) {
        ArrayList<Order> orders = new ArrayList<>();
        User user = ctx.sessionAttribute("currentUser");
        try {
            orders = OrderMapper.getOrdersByUserID(user.getUser_id(), connectionPool);
            ctx.sessionAttribute("Orders", orders);
            ctx.render("orderPage.html");
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteOrder(Context ctx, ConnectionPool connectionPool) {
        int orderID = Integer.parseInt(ctx.formParam("order_id"));

        try {
            OrderMapper.deleteOrderItem(orderID, connectionPool);
            OrderMapper.deleteOrder(orderID, connectionPool);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
        ctx.redirect("viewAccount");
    }

    public static void acceptOffer(Context ctx, ConnectionPool connectionPool) {
        int orderID = Integer.parseInt(ctx.formParam("order_id"));

        try {
            OrderMapper.updateOrderStatus(orderID, "Paid", connectionPool);

        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
        ctx.redirect("viewAccount");
    }


}