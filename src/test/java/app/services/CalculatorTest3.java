package app.services;

import app.entities.CarportDesign;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest3 {

    @Test
    void calcRafters() {

        // Laver en ny carport design.
        CarportDesign carportDesign = new CarportDesign(600, 400, "Rooftiles", 0, 0, "builder bob");

        // Beregn antal og længden af spær
        //int[] numberOfRafters = Calculator.calcRafters();
        //int actualNumberOfRafters = numberOfRafters[0];

        // forventet antal spær baseret på tilpassede afstande
        //assertEquals(7, actualNumberOfRafters);

    }
}