package app.services;

import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.MaterialMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;




public class Calculator {

    private final int POSTS = 6;
    private final int RAFTERS = 5;
    private final int BEAMS = 5;
    private List<Order_item> orderItems = new ArrayList<>();
    private static CarportDesign carportdesign;
    private ConnectionPool connectionPool;
    private int order_id;
    public Calculator(CarportDesign carportdesign, int order_id, ConnectionPool connectionPool)
    {
        this.order_id = order_id;
        this.carportdesign = carportdesign;
        this.connectionPool = connectionPool;
    }

    public void calcCarport() {
      calcPost();
      calcBeams();
      calcRafters();
    }

    // Stolperne = Post
    // TODO: Beregn antallet af stolper, og find længden på stolperne dvs. variant.
    public void calcPost() {

        int quantity = calcPostQuantity();
        List<Variant> productVariants = null;
        try {
            productVariants = MaterialMapper.getVariantsByProductIdAndMinLength(0,POSTS, connectionPool);
        } catch (SQLException | DatabaseException e) {
            throw new RuntimeException(e);
        }
        Variant productVariant = productVariants.get(0);
          double price = quantity * (productVariant.getMaterial().getPrice() * (productVariant.getLength()/100));
        // Assuming price is a double
        Order_item orderItem = new Order_item(0, quantity, "Stolper nedgraves 90 cm i jorden", productVariant.getMaterial().getMaterial_id(), order_id, price);

        orderItems.add(orderItem);
    }

    public static int calcPostQuantity() {
        int length = carportdesign.getCarport_length(); // getLength() returns the length of the carport
        return 2 * (2 + (length - 130) / 340);
    }



    // Remmene = Beams
    // TODO: Beregn antallet af remme, og find længden på remmene dvs. variant.
    public void calcBeams() {
        int length = carportdesign.getCarport_length();

        int distanceBetweenPosts = 150;

        int distanceBetweenBeams = 340;

        int numberOfBeams = 2 * (2 + (length - 130) / distanceBetweenBeams);

        // Tilføj to ekstra remme i hver ende for at sikre tilstrækkelig støtte
        numberOfBeams += 2;

        List<Variant> productVariants = null;
        try {
            // TODO: Finde ud af algoritme til når remmene er længere end 600 cm, så skal der bruger mere end to.
            productVariants = MaterialMapper.getVariantsByProductIdAndMinLength(carportdesign.getCarport_length(), BEAMS, connectionPool);
        } catch (SQLException | DatabaseException e) {
            throw new RuntimeException(e);
        }

        Variant productVariant = productVariants.get(0);
        double price = numberOfBeams * (productVariant.getMaterial().getPrice() * (productVariant.getLength()/100));

        Order_item orderItem = new Order_item(0, numberOfBeams, "Remme i sider, sadles ned i stolper", productVariant.getMaterial().getMaterial_id(), order_id, price);

        orderItems.add(orderItem);
    }

    /*public static int calcBeamsLength(CarportDesign carportdesign) {
        int length = carportdesign.getCarport_length();
        int postsQuantity = calcPostQuantity();

        // Afstanden mellem de ydre stolper (inklusive de to ekstra stolper i hver ende)
        int outerPostsDistance = (postsQuantity - 1) * 340;

        // Den samlede længde af remmen er carportens længde minus afstanden mellem de ydre stolper
        int beamsLength = length - outerPostsDistance;

        return beamsLength;
    }*/


    // Spærene = Rafters
    // TODO: Beregn antallet af Spærer, og find længden på spærene dvs. variant.
    public void calcRafters() {

        int width = carportdesign.getCarport_width();
        int length = carportdesign.getCarport_length();

        // Her er afstanden mellem hvert spær på tværs, af carportens længde.
        int rafterSpacing = 60;

        // Beregn antallet af spær
        int numberOfRafters = (int) Math.ceil((double) length / rafterSpacing);

        // Længden af hvert spær,
        // sættes til bredden,da spærene strækker sig over hele bredden.
        int rafterLength = width;

        // Returner antallet af spær og længden af hver spær i et array
        //return new int[] { numberOfRafters, rafterLength };

        List<Variant> productVariants;
        try {
            productVariants = MaterialMapper.getVariantsByProductIdAndMinLength(carportdesign.getCarport_length(), RAFTERS, connectionPool);
        } catch (SQLException | DatabaseException e) {
            throw new RuntimeException(e);
        }

        Variant productVariant = productVariants.get(0);
        double price = numberOfRafters * (productVariant.getMaterial().getPrice() * (productVariant.getLength()/100));

        Order_item orderItem = new Order_item(0, numberOfRafters, "Spær, monteres på rem", productVariant.getMaterial().getMaterial_id(), order_id, price);

        orderItems.add(orderItem);
    }


    public List<Order_item> getOrderItems()
    {
        return orderItems;
    }
}
