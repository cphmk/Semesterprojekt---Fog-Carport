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
    private final int SHED = 0;
    private List<Order_item> orderItems = new ArrayList<>();
    private static CarportDesign carportdesign;
    private ConnectionPool connectionPool;
    private int order_id;

    public Calculator(CarportDesign carportdesign, int order_id, ConnectionPool connectionPool) {
        this.order_id = order_id;
        this.carportdesign = carportdesign;
        this.connectionPool = connectionPool;
    }

    public void calcCarport() {
        calcPost();
        calcBeams();
        calcRafters();
        calcShed();
    }

    // TODO: Beregner prisen på Stolper, opretter en ordrelinje med denne information
    //  og tilføjer den til en liste af ordrelinjer.
    public void calcPost() {
        int quantity = calcPostQuantity();
        List<Variant> productVariants = null;
        try {
            productVariants = MaterialMapper.getVariantsByProductIdAndMinLength(0, POSTS, connectionPool);
        } catch (SQLException | DatabaseException e) {
            throw new RuntimeException(e);
        }
        Variant productVariant = productVariants.get(0);
        double price = quantity * (productVariant.getMaterial().getPrice() * (productVariant.getLength() / 100));

        Order_item orderItem = new Order_item(0, quantity, "Stolper nedgraves 90 cm i jorden", productVariant.getMaterial().getMaterial_id(), order_id, price);
        orderItems.add(orderItem);
    }



    // TODO: Beregn antallet af stolper til Carporten
    public static int calcPostQuantity() {
        int length = carportdesign.getCarport_length();
        if (carportdesign.getRedskabsrum_width() == 0) {
            return 2 * (2 + (length - 130) / 340);
        } else {
            return (2 * (2 + (length - 130) / 340)) + 4;
        }
    }



    // TODO: Beregner prisen på Remme, opretter en ordrelinje med denne information
    //  og tilføjer den til en liste af ordrelinjer.
    public void calcBeams() {
        List<Variant> productVariants = null;
        try {
            productVariants = MaterialMapper.getVariantsByProductIdAndMinLength(carportdesign.getCarport_length(), BEAMS, connectionPool);
        } catch (SQLException | DatabaseException e) {
            throw new RuntimeException(e);
        }
        int numberOfBeams = calcBeamsQuantity();
        Variant productVariant = productVariants.get(0);
        double price = numberOfBeams * (productVariant.getMaterial().getPrice() * (productVariant.getLength() / 100));
        Order_item orderItem = new Order_item(0, numberOfBeams, "Remme i sider, sadles ned i stolper", productVariant.getMaterial().getMaterial_id(), order_id, price);
        orderItems.add(orderItem);
    }



    // TODO: Beregn antallet af remme til Carporten
    public static int calcBeamsQuantity() {
        int distanceOfBeams = 600; // længden af de vandrette bjælker (remme),
        int numberOfBeams = 2; // Starter med to remme (en på hver side)

        // Hvis carportens længde overstiger den remmens længde
        // på hver side, tilføj ekstra remme.
        if (carportdesign.getCarport_length() > distanceOfBeams) {
            numberOfBeams += (carportdesign.getCarport_length() - distanceOfBeams + distanceOfBeams - 1) / distanceOfBeams;
        }

        System.out.println("Carport Length: " + carportdesign.getCarport_length());
        System.out.println("Number of Beams: " + numberOfBeams);

        return numberOfBeams;
    }




    // TODO: Beregner prisen på spærene, opretter en ordrelinje med denne information
    //  og tilføjer den til en liste af ordrelinjer.
    public void calcRafters() {
        List<Variant> productVariants;
        try {
            productVariants = MaterialMapper.getVariantsByProductIdAndMinLength(carportdesign.getCarport_length(), RAFTERS, connectionPool);
        } catch (SQLException | DatabaseException e) {
            throw new RuntimeException(e);
        }
        int numberOfRafters = calcRaftersQuantity();
        Variant productVariant = productVariants.get(0);
        double price = numberOfRafters * (productVariant.getMaterial().getPrice() * (productVariant.getLength() / 100));
        Order_item orderItem = new Order_item(0, numberOfRafters, "Spær, monteres på rem", productVariant.getMaterial().getMaterial_id(), order_id, price);
        orderItems.add(orderItem);
    }



    // TODO: Beregner antallet af spær til Carporten.
    public static int calcRaftersQuantity() {
        int width = carportdesign.getCarport_width();
        int length = carportdesign.getCarport_length();
        int rafterSpacing = 55;
        int rafterLength = width;
        int numberOfRafters = (int) (Math.ceil((double) length / rafterSpacing) + 2);
        return numberOfRafters;
    }



    //TODO: Beregner kun materialerne til skuret
    // og tilføjer dem til orderItems-listen.
    public void calcShed() {
        int shedWidth = carportdesign.getRedskabsrum_width();
        int shedLength = carportdesign.getRedskabsrum_length();

        // Beregn antal stolper, remme og spær med skuret
        int shedPostQuantity = calcShedPostQuantity(shedWidth, shedLength);
        int shedBeamsQuantity = calcShedBeamsQuantity(shedWidth, shedLength);
        int shedRaftersQuantity = calcShedRaftersQuantity(shedWidth);

        // Beregn prisen for stolper, remme og spær til skuret
        double shedPostPrice = calcShedPostPrice(shedPostQuantity);
        double shedBeamsPrice = calcShedBeamsPrice(shedBeamsQuantity);
        double shedRaftersPrice = calcShedRaftersPrice(shedRaftersQuantity);


        /*
        //Posts
        List<Variant> productVariantsPosts;
        try {
            productVariantsPosts = MaterialMapper.getVariantsByProductIdAndMinLength(carportdesign.getCarport_length(), POSTS, connectionPool);
        } catch (SQLException | DatabaseException e) {
            throw new RuntimeException(e);
        }
        int numberOfRafters1 = calcRaftersQuantity();
        Variant productVariant1 = productVariantsPosts.get(0);
        double price1 = numberOfRafters1 * (productVariant1.getMaterial().getPrice() * (productVariant1.getLength() / 100));

        //Beams
        List<Variant> productVariantsBeams;
        try {
            productVariantsBeams = MaterialMapper.getVariantsByProductIdAndMinLength(carportdesign.getCarport_length(), BEAMS, connectionPool);
        } catch (SQLException | DatabaseException e) {
            throw new RuntimeException(e);
        }
        int numberOfRafters2 = calcRaftersQuantity();
        Variant productVariant2 = productVariantsBeams.get(0);
        double price2 = numberOfRafters2 * (productVariant2.getMaterial().getPrice() * (productVariant2.getLength() / 100));

        //Rafters
        List<Variant> productVariantsRafters;
        try {
            productVariantsRafters = MaterialMapper.getVariantsByProductIdAndMinLength(carportdesign.getCarport_length(), RAFTERS, connectionPool);
        } catch (SQLException | DatabaseException e) {
            throw new RuntimeException(e);
        }
        int numberOfRafters3 = calcRaftersQuantity();
        Variant productVariant3 = productVariantsRafters.get(0);
        double price3 = numberOfRafters3 * (productVariant3.getMaterial().getPrice() * (productVariant3.getLength() / 100));

        // Tilføj skurets komponenter til orderItems-listen
        orderItems.add(new Order_item(0, shedPostQuantity, "Stolper til skur", , order_id, shedPostPrice));
        orderItems.add(new Order_item(0, shedBeamsQuantity, "Remme til skur", BEAMS, order_id, shedBeamsPrice));
        orderItems.add(new Order_item(0, shedRaftersQuantity, "Spær til skur", RAFTERS, order_id, shedRaftersPrice));*/
    }



    // TODO: Beregner antallet af stolper til skur
    public int calcShedPostQuantity(int shedWidth, int shedLength) {
        int postSpacing = 190; // Afstanden mellem hver stolpe i skuret
        // Antal stolper i længden af skuret
        int lengthPosts = 2 + (shedLength - 130) / postSpacing;
        // Antal stolper i bredden af skuret
        int widthPosts = 2 + (shedWidth - 130) / postSpacing;
        // Samlet antal stolper
        return 2 * (lengthPosts + widthPosts);
    }



    // TODO Beregner antallet af remme til skur
    public int calcShedBeamsQuantity(int shedWidth, int shedLength) {

        // Længden af en enkelt rem er 480 cm
        int beamLength = 480;

        // Beregning af antal remme langs skurets bredde og længde.
        int beamsAlongWidth = (int) Math.ceil((double) shedWidth / beamLength);
        int beamsAlongLength = (int) Math.ceil((double) shedLength / beamLength);

        // Vi skal bruge mindst to remme på hver side,
        // så vi tager det højeste antal som minimum
        int totalBeams = Math.max(2 * beamsAlongWidth, 2 * beamsAlongLength);

        return totalBeams;
    }




    // TODO Beregner antallet af spær til skur
    public int calcShedRaftersQuantity(int shedWidth) {

        return (int) Math.ceil((double) shedWidth / 55) + 1; // Vi skal inkludere begge ender
    }



    // TODO: Beregner prisen for en stolpe til skuret
    public double calcShedPostPrice(int shedPostQuantity) {
        // Prisen for en stolpe
        double postPrice = 36.95; // Eksempelpris, erstat med den faktiske pris
        return shedPostQuantity * postPrice;
    }



    // TODO: Beregner prisen for en rem til skuret
    public double calcShedBeamsPrice(int shedBeamsQuantity) {
        double beamPrice = 41.5;
        return shedBeamsQuantity * beamPrice;
    }



    // TODO: Beregner prisen for en spær til skuret
    public double calcShedRaftersPrice(int shedRaftersQuantity) {
        // Prisen for et spær
        double rafterPrice = 41.5;
        return shedRaftersQuantity * rafterPrice;
    }




    public List<Order_item> getOrderItems() {
        return orderItems;
    }
}


