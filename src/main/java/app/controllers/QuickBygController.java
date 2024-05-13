package app.controllers;

import app.entities.CarportDesign;
import app.entities.ContactInformation;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.CarportMapper;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class QuickBygController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/QuickByg", ctx -> ctx.render("QuickBygFrontpage.html"));
        app.get("/QuickByg/FladtTag", ctx -> ctx.render("QuickBygFladtTag.html"));
        app.get("/QuickByg/HoejtTag", ctx -> ctx.render("QuickBygOplysninger.html"));
        app.post("/QuickByg/Carport", ctx -> saveCarport(ctx));
        app.get("/QuickByg/Oplysninger", ctx -> Oplysninger(ctx, connectionPool));
        app.post("/QuickByg/Bestil",ctx -> OrderCarport(ctx,connectionPool));

    }

    private static void saveCarport(Context ctx) {
        CarportDesign carportDesign;

        int carportWidth = Integer.parseInt(ctx.formParam("carport_bredde"));
        int carportLength = Integer.parseInt(ctx.formParam("carport_længde"));
        int redskabsrumWidth = Integer.parseInt(ctx.formParam("redskabsrum_bredde"));
        int redskabsrumLength = Integer.parseInt(ctx.formParam("redskabsrum_længde"));
        String kommentar = ctx.formParam("kommentar");

        String tagPlader = ctx.formParam("carport_tag");
        if (tagPlader != null) {
            carportDesign = new CarportDesign(carportWidth,carportLength,tagPlader,redskabsrumWidth,redskabsrumLength,kommentar);
        }
        else {
            String tagType = ctx.formParam("tagtype");
            int tagHældning = Integer.parseInt(ctx.formParam("taghældning"));
            carportDesign = new CarportDesign(carportWidth,carportLength,tagType,tagHældning,redskabsrumWidth,redskabsrumLength,kommentar);
        }
        ctx.sessionAttribute("Carport",carportDesign);


        ctx.redirect("/QuickByg/Oplysninger");
    }

    private static void Oplysninger(Context ctx, ConnectionPool connectionPool) {
        //Get already existing information from the user
        User user = ctx.sessionAttribute("currentUser");

        if (user == null) {
            ctx.redirect("/login");
        }

        if (user.getContactInformation() != null) {
            ctx.attribute("name",user.getContactInformation().getName());
            ctx.attribute("address",user.getContactInformation().getAddress());
            ctx.attribute("postal_code",user.getContactInformation().getPostal_code());
            ctx.attribute("city",user.getContactInformation().getCity());
            ctx.attribute("phone_number",user.getContactInformation().getPhone_number());
            ctx.attribute("email",user.getContactInformation().getEmail());
        }

        ctx.render("QuickBygOplysninger.html");
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
        if (user.getContactInformation() == null) {
            ContactInformation contactInformation = new ContactInformation(name,address,postal_code,city,phone_number,email);
            user.setContactInformation(contactInformation);

            try {
                UserMapper.insertContactInformation(user,connectionPool);
            } catch (DatabaseException e) {
                throw new RuntimeException(e);
            }
        }

        else {
            try {
                UserMapper.updateContactInformation(user,connectionPool);
            } catch (DatabaseException e) {
                throw new RuntimeException(e);
            }
        }

        CarportDesign carportDesign = ctx.sessionAttribute("Carport");

        int carport_id;

        //Opret carport design i db
        try {
            carport_id = CarportMapper.createCarportDesign(carportDesign,connectionPool);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }

        //Opret ordre i db
        try {
           int order_id = OrderMapper.addOrder(carportDesign, user.getUser_id(), carport_id, connectionPool);
           ctx.sessionAttribute("order_id",order_id);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }

        ctx.sessionAttribute("currentUser",user);
        ctx.render("QuickBygFinish.html");
    }

}
