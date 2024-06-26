package app;

import app.config.SessionConfig;
import app.config.ThymeleafConfig;
import app.controllers.QuickBygController;

import app.controllers.AdminController;

import app.controllers.OrderController;

import app.controllers.UserController;
import app.entities.Order;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

public class Main {

    private static final String USER = "postgres";
    private static final String PASSWORD = "postgrespostgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "fogprojekt";

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    public static void main(String[] args) {
        // Initializing Javalin and Jetty webserver

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            config.jetty.modifyServletContextHandler(handler -> handler.setSessionHandler(SessionConfig.sessionConfig()));
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(7070);

        // Routing


        app.get("/", ctx -> ctx.render("QuickBygFrontpage.html"));
        QuickBygController.addRoutes(app,connectionPool);
        UserController.addRoutes(app, connectionPool);
        AdminController.addRoutes(app, connectionPool);
        OrderController.addRoutes(app,connectionPool);

    }
}