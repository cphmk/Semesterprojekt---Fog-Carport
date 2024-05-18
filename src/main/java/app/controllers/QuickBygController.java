package app.controllers;

import app.entities.CarportDesign;
import app.entities.ContactInformation;
import app.entities.Order_item;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.CarportMapper;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import app.persistence.UserMapper;
import app.services.Calculator;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.sql.SQLException;
import java.util.List;

public class QuickBygController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/QuickByg", ctx -> ctx.render("QuickBygFrontpage.html"));
        app.post("/QuickByg", ctx -> ctx.render("QuickBygFrontpage.html"));
        app.get("/QuickByg/FladtTag", ctx -> ctx.render("QuickBygFladtTag.html"));
        app.get("/QuickByg/HoejtTag", ctx -> ctx.render("QuickBygHoejtTag.html"));
        app.post("/QuickByg/Carport", ctx -> saveCarport(ctx));
        app.get("/QuickByg/Oplysninger", ctx -> Oplysninger(ctx, connectionPool));
        app.post("/QuickByg/Bestil", ctx -> OrderCarport(ctx, connectionPool));
    }

    private static void saveCarport(Context ctx) {
        CarportDesign carportDesign;

        int carportWidth = Integer.parseInt(ctx.formParam("carport_bredde"));
        int carportLength = Integer.parseInt(ctx.formParam("carport_længde"));
        String kommentar = ctx.formParam("kommentar");

        int redskabsrumWidth;
        int redskabsrumLength;

        if (ctx.formParam("redskabsrum_bredde") == null && ctx.formParam("redskabsrum_længde") == null) {
            redskabsrumWidth = 0;
            redskabsrumLength = 0;
        }
        else {
            redskabsrumWidth = Integer.parseInt(ctx.formParam("redskabsrum_bredde"));
            redskabsrumLength = Integer.parseInt(ctx.formParam("redskabsrum_længde"));
        }

        String tagPlader = ctx.formParam("carport_tag");

        //Fladt tag
        if (tagPlader != null) {
            carportDesign = new CarportDesign(carportWidth, carportLength, tagPlader, redskabsrumWidth, redskabsrumLength, kommentar);
        }

        //Højt tag
        else {
            String tagType = ctx.formParam("tagtype");
            int tagHældning = Integer.parseInt(ctx.formParam("taghældning"));
            carportDesign = new CarportDesign(carportWidth, carportLength, tagType, tagHældning, redskabsrumWidth, redskabsrumLength, kommentar);
        }
        ctx.sessionAttribute("Carport", carportDesign);


        ctx.redirect("/QuickByg/Oplysninger");
    }

    private static void Oplysninger(Context ctx, ConnectionPool connectionPool) {
        //Get already existing information from the user
        User user = ctx.sessionAttribute("currentUser");

        //Hvis man ikke er logget ind
        if (user == null) {
            ctx.redirect("/login");
        }

        //Hvis man er logget ind
        else {
            if (user.getContactInformation() != null) {
                ctx.attribute("name", user.getContactInformation().getName());
                ctx.attribute("address", user.getContactInformation().getAddress());
                ctx.attribute("postal_code", user.getContactInformation().getPostal_code());
                ctx.attribute("city", user.getContactInformation().getCity());
                ctx.attribute("phone_number", user.getContactInformation().getPhone_number());
                ctx.attribute("email", user.getContactInformation().getEmail());
            }
            ctx.render("QuickBygOplysninger.html");
        }

    }

    private static void OrderCarport(Context ctx, ConnectionPool connectionPool) {
        String name = ctx.formParam("navn");
        String address = ctx.formParam("adresse");
        int postal_code = Integer.parseInt(ctx.formParam("postnummer"));
        String city = ctx.formParam("by");
        int phone_number = Integer.parseInt(ctx.formParam("telefon"));
        String email = ctx.formParam("email");


        //Opret eller opdater kontakt informationer
        User user = ctx.sessionAttribute("currentUser");

            //Hvis der ikke eksisteret noget contact information
            if (user.getContactInformation() == null) {
                user.setContactInformation(new ContactInformation(name, address, postal_code, city, phone_number, email));
                try {
                    UserMapper.insertContactInformation(user, connectionPool);
                } catch (DatabaseException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                user.setContactInformation(new ContactInformation(name,address,postal_code,city,phone_number,email));
                try {
                    UserMapper.updateContactInformation(user, connectionPool);
                } catch (DatabaseException e) {
                    throw new RuntimeException(e);
                }
            }


        //Carport design
        CarportDesign carportDesign = ctx.sessionAttribute("Carport");

        //Carport id
        int carport_id;

        //Opret carport design i db
        try {
            carport_id = CarportMapper.createCarportDesign(carportDesign, connectionPool);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }

        //Ordre id
        int order_id;

        //Opret ordre i db
        try {
            order_id = OrderMapper.addOrder(carportDesign, user.getUser_id(), carport_id, connectionPool);
            ctx.sessionAttribute("order_id", order_id);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }

        // Calculate order items using Calculator
        Calculator calculator = new Calculator(carportDesign, order_id, connectionPool);
        calculator.calcCarport();
        List<Order_item> orderItems = calculator.getOrderItems();

        // Save order items in the database
        try {
            OrderMapper.addOrderItems(orderItems, connectionPool);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }

        //Get all the price from the order items and add to order.
        double price = 0;
        for (Order_item orderItem : orderItems) {
            price += orderItem.getPrice();
        }
        try {
            OrderMapper.updateOrderPrice(order_id,price,connectionPool);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }

        ctx.sessionAttribute("currentUser", user);
        ctx.render("QuickBygFinish.html");
    }

}
