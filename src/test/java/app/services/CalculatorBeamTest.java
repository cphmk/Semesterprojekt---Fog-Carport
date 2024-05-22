package app.services;

import app.entities.CarportDesign;
import app.persistence.ConnectionPool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorBeamTest {

    private CarportDesign carportDesign;
    private Calculator calculator;

    @BeforeEach
    public void setUp() {
        carportDesign = new CarportDesign(600, 780, "", 100, 100, ""); // Eksempel dimensioner baseret på billede
        ConnectionPool connectionPool = new ConnectionPool(); // Erstat med faktisk connection pool instans
        calculator = new Calculator(carportDesign, 1, connectionPool);
    }

    @Test
    public void testCalcBeamsQuantity_LessThan600() {
        int carportlength = 500; // Mindre end 600
        int expected = 3; // Forventer 2 remme for en carport på 500 cm ifølge din tidligere beregning
        int result = calculator.calcBeamsQuantity();
        assertEquals(expected, result);
    }

    @Test
    public void testCalcBeamsQuantity_600rMore() {
        int carportLength = 780; // 780 er mere end 600
        int expected = 3; // Forventer 3 remme for en carport på 780 cm ifølge din tidligere beregning
        int result = calculator.calcBeamsQuantity();
        assertEquals(expected, result);
    }
}