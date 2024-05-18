package app.services;

import app.entities.CarportDesign;
import app.persistence.ConnectionPool;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculatorTest {

    static Calculator calculator;

    @BeforeAll
    static void setup() {

        //Vi lader dem være tomme, og bruger environment variabler til at forbinde i stedet.
        String USER = "";
        String PASSWORD = "";
        String URL = "";
        String DB = "";

        ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);
        CarportDesign carportDesign = new CarportDesign(600,780,"",300,300,"");
        calculator = new Calculator(carportDesign,0,connectionPool);
    }

    @Test
    void calcPostQuantity() {

        //Vi tager 10, som i udgangspunkt i carport eksempelet fra fog
        int expected = 10;
        int result = Calculator.calcPostQuantity();

        assertEquals(expected,result);



        /*// TODO: Oprettet en instans af CarportDesign, som du vil bruge til testen
        CarportDesign carportDesign = new CarportDesign(600, 700,"Rooftiles", 0,0, "builder bob");

        // TODO: Expected number of posts based on the given carport design
        int expectedQuantity = 2 * (2 + (carportDesign.getCarport_length() - 130) / 340);

        // TODO: Create an instance of Calculator and call calcPostQuantity() with carportDesign
        Calculator calculator = new Calculator(carportDesign, 0, null);
        int actualQuantity = calculator.calcPostQuantity();

        // TODO: Compare the expected result with the actual result.
        assertEquals(expectedQuantity, actualQuantity);*/
    }

    @Test
    void calcBeamsQuantity() {
        //Vi tager 3, som i udgangspunkt i carport eksemplet fra fog
        int expected = 3;
        int result = Calculator.calcBeamsQuantity();

        assertEquals(expected,result);
    }

    @Test
    void calcRaftersQuantity() {
        //Vi tager 15, som i udgangspunkt i carport eksemplet fra fog
        int expected = 15;
        int result = Calculator.calcRaftersQuantity();

        assertEquals(expected,result);
    }

    //Ved at sammenligne det forventede antal stolper med det faktiske antal stolper,
    // som metoden returnerer, kan du validere, om din beregning er korrekt.
}