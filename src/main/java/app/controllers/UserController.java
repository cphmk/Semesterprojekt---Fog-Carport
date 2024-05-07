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
        app.get("login", ctx -> ctx.render("loginpage.html"));
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
        String address = ctx.formParam("address");

        if (cpassword.equals(password)) {
            try {
                UserMapper.createuser(username, password, address, connectionPool);
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
            if (user.getRole()) {
                ctx.redirect("adminView");
            } else
                ctx.render("QuickBygFrontpage.html");
            } catch(DatabaseException e){
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
}
