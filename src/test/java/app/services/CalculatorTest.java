package app.services;

import app.entities.CarportDesign;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculatorTest {

    @Test
    void calcPostQuantity() {

        // TODO: Oprettet en instans af CarportDesign, som du vil bruge til testen
        CarportDesign carportDesign = new CarportDesign(600, 700,"Rooftiles", 0,0, "builder bob");

        // TODO: Expected number of posts based on the given carport design
        int expectedQuantity = 2 * (2 + (carportDesign.getCarport_length() - 130) / 340);

        // TODO: Create an instance of Calculator and call calcPostQuantity() with carportDesign
        Calculator calculator = new Calculator(carportDesign, 0, null);
        int actualQuantity = calculator.calcPostQuantity();

        // TODO: Compare the expected result with the actual result.
        assertEquals(expectedQuantity, actualQuantity);
    }

    //Ved at sammenligne det forventede antal stolper med det faktiske antal stolper,
    // som metoden returnerer, kan du validere, om din beregning er korrekt.
}