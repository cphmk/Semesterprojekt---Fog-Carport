package app.services;

import app.entities.CarportDesign;
import app.persistence.ConnectionPool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorRafterTest {

    private CarportDesign carportDesign;
    private Calculator calculator;

    @BeforeEach
    public void setUp() {
        carportDesign = new CarportDesign(500, 700,"", 100, 100,"" ); // Eksempel dimensioner
        ConnectionPool connectionPool = new ConnectionPool(); // Erstat med faktisk connection pool instans
        calculator = new Calculator(carportDesign, 1, connectionPool);
    }

    @Test
    public void testCalcRaftersQuantity() {
        int expected = 15; // Justeret forventet værdi
        int result = Calculator.calcRaftersQuantity();
        assertEquals(expected, result);
    }
}
