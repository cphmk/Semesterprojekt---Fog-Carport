package app.controllers;

import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class AdminController {
    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.post("login", ctx -> LoginAdmin(ctx, connectionPool));
        app.get("login", ctx -> ctx.render("login.html"));
    }
    public static void LoginAdmin(Context ctx, ConnectionPool connectionPool) {
        // Implement logic to check if the user is an admin
        // Hent form parametre
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");
        boolean admin = ctx.sessionAttribute("admin");
        // Retrieve user details from the database and check if the admin flag is true
        // Check om bruger findes i DB med de angivne username + password
        try {
            User user = UserMapper.loginAdmin(username, password, admin, connectionPool);
            ctx.sessionAttribute("currentUser", user);
            // Hvis ja, send videre til forsiden med login besked
            ctx.render("admin.html");
        } catch (DatabaseException e) {
            // Hvis nej, send tilbage til login side med fejl besked
            if (e.getMessage().equals("DB fejl")) {
                ctx.attribute("message", "Can't connect to db");
            }
            if (e.getMessage().equals("Fejl i login. Prøv igen")) {
                ctx.attribute("message", "Wrong username or password");
            }
            ctx.render("login.html");
        }
        // return user.isAdmin();
    }
}
