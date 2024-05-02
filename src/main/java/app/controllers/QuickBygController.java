package app.controllers;

import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class QuickBygController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("QuickByg", ctx -> ctx.render("QuickBygFrontpage.html"));
        app.get("QuickByg/FladtTag", ctx -> ctx.render("QuickBygFladtTag.html"));
        app.get("QuickByg/HoejtTag", ctx -> ctx.render("QuickBygHoejtTag.html"));
        app.get("QuickByg/Oplysninger", ctx -> contact(ctx));
    }

    private static void contact(Context ctx) {
        ctx.render("QuickBygOplysninger.html");
    }
}
