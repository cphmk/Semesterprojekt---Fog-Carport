package app.services;

import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.MaterialMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;




public class Calculator {

    private final int POSTS = 1;
    private final int RAFTERS = 2;
    private final int BEAMS = 2;
    private List<Order_item> orderItems = new ArrayList<>();
    private CarportDesign carportdesign;
    private ConnectionPool connectionPool;
    private int order_id;
    public Calculator(CarportDesign carportdesign, int order_id, ConnectionPool connectionPool)
    {
        this.order_id = order_id;
        this.carportdesign = carportdesign;
        this.connectionPool = connectionPool;
    }

    public void calcCarport(CarportDesign carportdesign) throws DatabaseException, SQLException {
      calcPost(carportdesign);
      calcBeams(carportdesign);
      calcRafters(carportdesign);
    }

    // Stolperne = Post
    // TODO: Beregn antallet af stolper, og find længden på stolperne dvs. variant.
    public void calcPost(CarportDesign carportdesign) throws SQLException, DatabaseException {

        int quantity = calcPostQuantity(carportdesign);
        List<Variant> productVariants = MaterialMapper.getVariantsByProductIdAndMinLength(0,POSTS, connectionPool);
          Variant productVariant = productVariants.get(0);
          double price = quantity * (productVariant.getMaterial().getPrice() * (productVariant.getLength()/100));
        // Assuming price is a double
        Order_item orderItem = new Order_item(0, quantity, "Stolper nedgraves 90 cm i jorden", productVariant.getMaterial().getMaterial_id(), order_id, price);

        orderItems.add(orderItem);
    }

    public static int calcPostQuantity(CarportDesign carportdesign) {
        int length = carportdesign.getCarport_length(); // getLength() returns the length of the carport
        return 2 * (2 + (length - 130) / 340);
    }



    // Remmene = Beams
    // TODO: Beregn antallet af remme, og find længden på remmene dvs. variant.
    public static int calcBeams(CarportDesign carportdesign) {
        int length = carportdesign.getCarport_length();

        int distanceBetweenPosts = 150;

        int distanceBetweenBeams = 340;

        int numberOfBeams = 2 * (2 + (length - 130) / distanceBetweenBeams);

        // Tilføj to ekstra remme i hver ende for at sikre tilstrækkelig støtte
        numberOfBeams += 2;

        return numberOfBeams;
    }


    // Spærene = Rafters
    // TODO: Beregn antallet af Spærer, og find længden på spærene dvs. variant.
    public static int[] calcRafters(CarportDesign carportdesign) {

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
        return new int[] { numberOfRafters, rafterLength };
    }


    public List<Order_item> getOrderItems()
    {
        return orderItems;
    }
}
