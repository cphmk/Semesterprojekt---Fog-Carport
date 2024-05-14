package app.services;

import app.entities.CarportDesign;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest2 {


    @Test
    void calcBeams_customDistance() {
        // Laver en ny carport design.
        CarportDesign carportDesign = new CarportDesign(600, 400, "Rooftiles", 0, 0, "builder bob");

        // Aktuelle nummer af remme
        //int numberOfBeams = Calculator.calcBeams();

        // forventet antal remme baseret på tilpassede afstande
        //assertEquals(6, numberOfBeams);
    }

}
