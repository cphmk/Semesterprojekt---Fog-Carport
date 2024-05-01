package app.controllers;


import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class UserController {
    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.post("login", ctx -> login(ctx, connectionPool));
        app.get("login", ctx -> ctx.render("login.html"));
        app.get("logout", ctx -> logout(ctx));
        app.get("signup", ctx -> ctx.render("signup.html"));
        app.post("signup", ctx -> signup(ctx, connectionPool));
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
                ctx.redirect("login");
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
            ctx.sessionAttribute("currentUser", user);
            // Hvis ja, send videre til forsiden med login besked
            ctx.render("buypage.html");
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
    }
}
