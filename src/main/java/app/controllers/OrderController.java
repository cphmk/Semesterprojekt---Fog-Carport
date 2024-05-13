package app.controllers;

import app.entities.CarportDesign;
import app.persistence.ConnectionPool;
import app.services.CarportSvg;
import app.services.Svg;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.Locale;

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

        ctx.attribute("svg", svg.toString());
        ctx.render("showOrder.html");
    }

}

