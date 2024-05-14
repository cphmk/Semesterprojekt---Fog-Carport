package app.controllers;

import app.entities.CarportDesign;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import app.services.CarportSvg;
import app.entities.Order_item;
import app.exceptions.DatabaseException;
import app.services.Calculator;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

public class OrderController {
    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/showOrder", ctx -> showOrder(ctx));
    }

    public static void showOrder(Context ctx) {
        // TODO: Create a SVG Drawing and inject into the showOrder.html template as a String
        Locale.setDefault(new Locale("US"));
        CarportDesign carportDesign = new CarportDesign(600, 780, "", 530, 390, "");
        CarportSvg svg = new CarportSvg(carportDesign);
    }
}

