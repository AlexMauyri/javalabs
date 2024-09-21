package ru.ssau.tk.DoubleA.javalabs.operations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ssau.tk.DoubleA.javalabs.exceptions.InconsistentFunctionsException;
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

    @Test
    void doOperationMultiplyTest() {
        TabulatedFunctionOperationService operationService = new TabulatedFunctionOperationService();

        Assertions.assertThrows(InconsistentFunctionsException.class, () -> operationService.multiply(tabulatedFunction1, tabulatedFunction2));
        Assertions.assertThrows(InconsistentFunctionsException.class, () -> operationService.multiply(tabulatedFunction2, tabulatedFunction3));

        tabulatedFunction2 = new LinkedListTabulatedFunction(new double[]{1.0, 2.5, 3.5, 5.0}, new double[]{0.5, 3.8, -5.3, 2.7});

        Assertions.assertArrayEquals(new Point[] {
                new Point(1.0, 0.7),
                new Point(2.5, 12.92),
                new Point(3.5, -13.25),
                new Point(5.0, 7.29)
            }, TabulatedFunctionOperationService.asPoints(operationService.multiply(tabulatedFunction1, tabulatedFunction2)));
        Assertions.assertArrayEquals(new Point[] {
                new Point(1.0, 0.7),
                new Point(2.5, 12.92),
                new Point(3.5, -13.25),
                new Point(5.0, 7.29)
        }, TabulatedFunctionOperationService.asPoints(operationService.multiply(tabulatedFunction2, tabulatedFunction1)));
    }

    @Test
    void doOperationDivideTest() {
        TabulatedFunctionOperationService operationService = new TabulatedFunctionOperationService();

        Assertions.assertThrows(InconsistentFunctionsException.class, () -> operationService.divide(tabulatedFunction1, tabulatedFunction2));
        Assertions.assertThrows(InconsistentFunctionsException.class, () -> operationService.divide(tabulatedFunction2, tabulatedFunction3));

        tabulatedFunction2 = new LinkedListTabulatedFunction(new double[]{1.0, 2.5, 3.5, 5.0}, new double[]{0.5, 3.8, -5.3, 2.7});

        Assertions.assertArrayEquals(new Point[] {
                new Point(1.0, 2.8),
                new Point(2.5, 0.8947368421052),
                new Point(3.5, -0.4716981132075),
                new Point(5.0, 1.0)
        }, TabulatedFunctionOperationService.asPoints(operationService.divide(tabulatedFunction1, tabulatedFunction2)));
        Assertions.assertArrayEquals(new Point[] {
                new Point(1.0, 0.3571428571428),
                new Point(2.5, 1.1176470588235),
                new Point(3.5, -2.12),
                new Point(5.0, 1.0)
        }, TabulatedFunctionOperationService.asPoints(operationService.divide(tabulatedFunction2, tabulatedFunction1)));
    }

    @Test
    void doOperationTest() {
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();

        Assertions.assertArrayEquals(
                new Point[] {
                        new Point(1.0, 2.8),
                        new Point(2.5, 6.8),
                        new Point(3.5, 5.0),
                        new Point(5.0, 5.4)
                },
                TabulatedFunctionOperationService.asPoints(
                        service.addition(tabulatedFunction1, tabulatedFunction1)
                )
        );

        tabulatedFunction2 = new LinkedListTabulatedFunction(
                new double[]{1.0, 2.5, 3.5, 5.0},
                new double[]{1.6, 3.5, 1.4, 6.4}
        );

        Assertions.assertArrayEquals(
                new Point[] {
                        new Point(1.0, 3.0),
                        new Point(2.5, 6.9),
                        new Point(3.5, 3.9),
                        new Point(5.0, 9.1)
                },
                TabulatedFunctionOperationService.asPoints(
                        service.addition(tabulatedFunction1, tabulatedFunction2)
                )
        );

        Assertions.assertArrayEquals(
                new Point[] {
                        new Point(1.0, 0.2),
                        new Point(2.5, 0.1),
                        new Point(3.5, -1.1),
                        new Point(5.0, 3.7)
                },
                TabulatedFunctionOperationService.asPoints(
                        service.subtraction(tabulatedFunction2, tabulatedFunction1)
                )
        );

        tabulatedFunction2 = new LinkedListTabulatedFunction(
                new double[]{1.0, 2.5, 3.6, 5.0},
                new double[]{1.6, 3.5, 1.4, 6.4}
        );

        Assertions.assertThrows(InconsistentFunctionsException.class,
                () -> service.addition(tabulatedFunction1, tabulatedFunction2));
        Assertions.assertThrows(InconsistentFunctionsException.class,
                () -> service.subtraction(tabulatedFunction1, tabulatedFunction2));

        tabulatedFunction1 = new LinkedListTabulatedFunction(
                new double[]{1.0, 2.5, 3.6, 5.0},
                new double[]{2.4, 1.5, 0.0, -1.5}
        );

        Assertions.assertArrayEquals(
                new Point[] {
                        new Point(1.0, 4.0),
                        new Point(2.5, 5.0),
                        new Point(3.6, 1.4),
                        new Point(5.0, 4.9)
                },
                TabulatedFunctionOperationService.asPoints(
                        service.addition(tabulatedFunction2, tabulatedFunction1)
                )
        );

        Assertions.assertArrayEquals(
                new Point[] {
                        new Point(1.0, -0.8),
                        new Point(2.5, 2.0),
                        new Point(3.6, 1.4),
                        new Point(5.0, 7.9)
                },
                TabulatedFunctionOperationService.asPoints(
                        service.subtraction(tabulatedFunction2, tabulatedFunction1)
                )
        );

        tabulatedFunction1 = new StrictTabulatedFunction(
                new UnmodifiableTabulatedFunction(
                        new LinkedListTabulatedFunction(
                                new double[]{1.0, 2.5, 3.6, 5.0},
                                new double[]{2.4, 1.5, 0.0, -1.5}
                        )
                )
        );

        tabulatedFunction2 = new UnmodifiableTabulatedFunction(
                new LinkedListTabulatedFunction(
                        new double[]{1.0, 2.5, 3.6, 5.0},
                        new double[]{1.6, 3.5, 1.4, 6.4}
                )
        );

        Assertions.assertArrayEquals(
                new Point[] {
                        new Point(1.0, 4.0),
                        new Point(2.5, 5.0),
                        new Point(3.6, 1.4),
                        new Point(5.0, 4.9)
                },
                TabulatedFunctionOperationService.asPoints(
                        service.addition(tabulatedFunction2, tabulatedFunction1)
                )
        );

        Assertions.assertArrayEquals(
                new Point[] {
                        new Point(1.0, -0.8),
                        new Point(2.5, 2.0),
                        new Point(3.6, 1.4),
                        new Point(5.0, 7.9)
                },
                TabulatedFunctionOperationService.asPoints(
                        service.subtraction(tabulatedFunction2, tabulatedFunction1)
                )
        );
    }
}
