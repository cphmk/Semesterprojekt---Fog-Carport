package app.services;

import app.entities.CarportDesign;
import app.persistence.ConnectionPool;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculatorTest {

    static ConnectionPool connectionPool = ConnectionPool.getInstance("", "", "", "");
    static Calculator calculator;


    @BeforeAll
    static void setup() {
        //Carport 600x780 without shed
        CarportDesign carportDesign = new CarportDesign(600,780,"",600,210,"");
        calculator = new Calculator(carportDesign,0,connectionPool);
    }

    @Test
    void calcPostQuantity() {
        //Vi tager 10, som i udgangspunkt i carport eksempelet fra fog
        int result = Calculator.calcPostQuantity();
        assertEquals(10,result);

    }

    @Test
    void calcBeamsQuantity() {
        //Vi tager 3, som i udgangspunkt i carport eksemplet fra fog
        int result = Calculator.calcBeamsQuantity();
        assertEquals(3,result);

    }

    @Test
    void calcRaftersQuantity() {
        //Vi tager 15, som i udgangspunkt i carport eksemplet fra fog
        int result = Calculator.calcRaftersQuantity();
        assertEquals(15,result);

    }
}