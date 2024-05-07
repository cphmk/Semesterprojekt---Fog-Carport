package app.controllers;

import app.services.CarportSvg;
import app.services.Svg;
import io.javalin.http.Context;

import java.util.Locale;

public class OrderController
{
    public static void showOrder(Context ctx)
    {
        // TODO: Create a SVG Drawing and inject into the showOrder.html template as a String
        Locale.setDefault(new Locale("US"));
        CarportSvg svg = new CarportSvg(600, 780);

        ctx.attribute("svg", svg.toString());
        ctx.render("showOrder.html");
    }

}

