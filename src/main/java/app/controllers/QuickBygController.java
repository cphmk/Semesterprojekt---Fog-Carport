package app.controllers;

import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class QuickBygController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("QuickByg", ctx -> ctx.render("QuickBygFrontpage.html"));
        app.get("QuickByg/FladtTag", ctx -> ctx.render("QuickBygFladtTag.html"));
        app.get("QuickByg/HoejtTag", ctx -> ctx.render("QuickBygHoejtTag.html"));
        app.post("/QuickByg/Oplysninger", ctx -> contact(ctx));
    }

    private static void contact(Context ctx) {

        /*int carportWidth = Integer.parseInt(ctx.formParam("carport_bredde"));
        int carportLength = Integer.parseInt(ctx.formParam("carport_længde"));
        String tagplader = ctx.formParam("carport_tag");

        int redskabsrumWidth = Integer.parseInt(ctx.formParam("redskabs_bredde"));
        int redskabsrumLength = Integer.parseInt(ctx.formParam("redskabs_længde"));

        String kommentar = ctx.formParam("kommentar");*/

        ctx.render("QuickBygOplysninger.html");
    }
}
