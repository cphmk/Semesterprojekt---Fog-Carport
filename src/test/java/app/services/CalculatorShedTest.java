package app.services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CalculatorShedTest {

    @Test
    void testCalcShedPostQuantity() {
        Calculator calculator = new Calculator(null, 0, null); // Opret en instans af Calculator med dummyværdier
        int shedPostQuantity = calculator.calcShedPostQuantity(300, 400);
        assertEquals(10, shedPostQuantity); // Tilpas dette tal baseret på den forventede output
    }

    @Test
    void testCalcShedBeamsQuantity() {
        Calculator calculator = new Calculator(null, 0, null); // Opret en instans af Calculator med dummyværdier
        int shedBeamsQuantity = calculator.calcShedBeamsQuantity(300, 400);
        assertEquals(2, shedBeamsQuantity); // Tilpas dette tal baseret på den forventede output
    }


    @Test
    void testCalcShedRaftersQuantity() {
        Calculator calculator = new Calculator(null, 0, null); // Opret en instans af Calculator med dummyværdier
        int shedRaftersQuantity = calculator.calcShedRaftersQuantity(300);
        assertEquals(7, shedRaftersQuantity);
    }

    @Test
    void testCalcShedPostPrice() {
        Calculator calculator = new Calculator(null, 0, null); // Opret en instans af Calculator med dummyværdier
        double shedPostPrice = calculator.calcShedPostPrice(12);
        assertEquals(443.40000000000003, shedPostPrice);
    }

    @Test
    void testCalcShedBeamsPrice() {
        Calculator calculator = new Calculator(null, 0, null); // Opret en instans af Calculator med dummyværdier
        double shedBeamsPrice = calculator.calcShedBeamsPrice(14);
        assertEquals(581.0, shedBeamsPrice); // Tilpas dette tal baseret på den forventede output
    }

    @Test
    void testCalcShedRaftersPrice() {
        Calculator calculator = new Calculator(null, 0, null); // Opret en instans af Calculator med dummyværdier
        double shedRaftersPrice = calculator.calcShedRaftersPrice(6);
        assertEquals(249.0, shedRaftersPrice); // Tilpas dette tal baseret på den forventede output
    }
}

