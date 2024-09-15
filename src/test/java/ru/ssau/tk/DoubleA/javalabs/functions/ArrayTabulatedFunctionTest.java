package ru.ssau.tk.DoubleA.javalabs.functions;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ArrayTabulatedFunctionTest extends AbstractTest {

    ArrayTabulatedFunction arrayTabulatedFunction;

    @BeforeEach
    void init() {
        arrayTabulatedFunction = new ArrayTabulatedFunction(new SqrFunction(), 1.0, 2.0, 6);
    }


    @Test
    void applyTest() {
        Assertions.assertTrue(Math.abs(2.26 - arrayTabulatedFunction.apply(1.5)) <= EPSILON);
        Assertions.assertTrue(Math.abs(1.96 - arrayTabulatedFunction.apply(1.4)) <= EPSILON);
        Assertions.assertTrue(Math.abs(7.8 - arrayTabulatedFunction.apply(3.0)) <= EPSILON);
        Assertions.assertTrue(Math.abs(-1.2 - arrayTabulatedFunction.apply(0)) <= EPSILON);
        arrayTabulatedFunction = new ArrayTabulatedFunction(new DerivativeFunction(new NRootCalculateFunction(5)), 10, 25, 20);
        Assertions.assertTrue(Math.abs(0.02982 - arrayTabulatedFunction.apply(10.7894)) <= EPSILON);
        Assertions.assertTrue(Math.abs(0.03050 - arrayTabulatedFunction.apply(10.5)) <= EPSILON);
        Assertions.assertTrue(Math.abs(0.01268 - arrayTabulatedFunction.apply(30.0)) <= EPSILON);
        Assertions.assertTrue(Math.abs(0.04353 - arrayTabulatedFunction.apply(5.0)) <= EPSILON);
    }

    @Test
    void getterTest() {
        Assertions.assertTrue(Math.abs(1.2 - arrayTabulatedFunction.getX(1)) <= EPSILON);
        Assertions.assertTrue(Math.abs(2.0 - arrayTabulatedFunction.getX(5)) <= EPSILON);
        Assertions.assertTrue(Math.abs(4.0 - arrayTabulatedFunction.getY(5)) <= EPSILON);
        Assertions.assertTrue(Math.abs(1.96 - arrayTabulatedFunction.getY(2)) <= EPSILON);
        Assertions.assertTrue(Math.abs(1.0 - arrayTabulatedFunction.leftBound()) <= EPSILON);
        Assertions.assertTrue(Math.abs(2.0 - arrayTabulatedFunction.rightBound()) <= EPSILON);
        Assertions.assertEquals(2, arrayTabulatedFunction.indexOfX(1.4));
        Assertions.assertEquals(-1, arrayTabulatedFunction.indexOfX(3.0));
        Assertions.assertEquals(5, arrayTabulatedFunction.indexOfY(4.0));
        Assertions.assertEquals(-1, arrayTabulatedFunction.indexOfY(9.0));
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> arrayTabulatedFunction.getX(10));
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> arrayTabulatedFunction.getY(-1));
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> arrayTabulatedFunction.getX(-1));
    }

    @Test
    void setterTest() {
        Assertions.assertDoesNotThrow(() -> arrayTabulatedFunction.setY(3, 1.5));
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> arrayTabulatedFunction.setY(10, 1.5));
    }

}
