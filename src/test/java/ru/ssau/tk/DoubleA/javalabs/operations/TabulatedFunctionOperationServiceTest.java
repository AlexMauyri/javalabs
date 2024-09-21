package ru.ssau.tk.DoubleA.javalabs.operations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ssau.tk.DoubleA.javalabs.functions.*;


public class TabulatedFunctionOperationServiceTest {
    TabulatedFunction tabulatedFunction1;
    TabulatedFunction tabulatedFunction2;
    TabulatedFunction tabulatedFunction3;

    @BeforeEach
    void init() {
        tabulatedFunction1 = new ArrayTabulatedFunction(
                new double[]{1.0, 2.5, 3.5, 5.0},
                new double[]{1.4, 3.4, 2.5, 2.7}
        );
        tabulatedFunction2 = new LinkedListTabulatedFunction(
                new double[]{-3, 1.5, 6, 10.5, 15},
                new double[]{9, 2.25, 36, 110.25, 225}
        );
        tabulatedFunction3 = new LinkedListTabulatedFunction(
                new double[]{9, 11},
                new double[]{18, 25}
        );
    }

    @Test
    void asPointsTest() {
        Assertions.assertArrayEquals(
                new Point[] {
                        new Point(1.0, 1.4),
                        new Point(2.5, 3.4),
                        new Point(3.5, 2.5),
                        new Point(5.0, 2.7)
                },
                TabulatedFunctionOperationService.asPoints(tabulatedFunction1)
        );

        Assertions.assertArrayEquals(
                new Point[] {
                        new Point(-3, 9),
                        new Point(1.5, 2.25),
                        new Point(6, 36),
                        new Point(10.5, 110.25),
                        new Point(15, 225)
                },
                TabulatedFunctionOperationService.asPoints(tabulatedFunction2)
        );

        Assertions.assertArrayEquals(
                new Point[] {
                        new Point(9, 18),
                        new Point(11, 25)
                },
                TabulatedFunctionOperationService.asPoints(tabulatedFunction3)
        );
    }
}
