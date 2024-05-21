package app.services;

import app.entities.CarportDesign;
import app.persistence.ConnectionPool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorPostTest {

    private CarportDesign carportDesign;
    private Calculator calculator;

    @BeforeEach
    public void setUp() {
        carportDesign = new CarportDesign(500, 700,"", 100, 100,"" ); // Eksempel dimensioner
        ConnectionPool connectionPool = new ConnectionPool(); // Erstat med faktisk connection pool instans
        calculator = new Calculator(carportDesign, 1, connectionPool);
    }

    @Test
    public void testCalcPostQuantity_NoShed() {
        carportDesign.setRedskabsrum_width(0); // Ingen skur
        int expected = 6;
        int result = Calculator.calcPostQuantity();
        assertEquals(expected, result);
    }

    @Test
    public void testCalcPostQuantity_WithShed() {
        carportDesign.setRedskabsrum_width(100); // Med skur
        int expected = 10;
        int result = Calculator.calcPostQuantity();
        assertEquals(expected, result);
    }
}


