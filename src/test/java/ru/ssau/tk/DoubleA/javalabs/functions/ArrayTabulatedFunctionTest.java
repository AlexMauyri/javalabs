package ru.ssau.tk.DoubleA.javalabs.functions;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ArrayTabulatedFunctionTest extends AbstractTest {

    ArrayTabulatedFunction mathFunctionDiscrete;
    ArrayTabulatedFunction mathFunctionManual;

    @BeforeEach
    void init() {
        mathFunctionDiscrete = new ArrayTabulatedFunction(new SqrFunction(), 1.0, 2.0, 6);
        mathFunctionManual = new ArrayTabulatedFunction(new double[]{1.0, 2.5, 3.5, 5.0}, new double[]{1.4, 3.4, 2.5, 2.7});
    }

    @Test
    void removeTest() {
        mathFunctionManual.remove(3);
        Assertions.assertEquals(3, mathFunctionManual.getCount());
        mathFunctionManual.remove(0);
        Assertions.assertEquals(2, mathFunctionManual.getCount());
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> mathFunctionManual.getX(3));
    }

    @Test
    void insertTest() {
        mathFunctionManual.insert(1.9, 5.6);
        Assertions.assertEquals(5, mathFunctionManual.getCount());
        Assertions.assertEquals(4, mathFunctionManual.emptyPlaces);
        mathFunctionManual.insert(0.6, -3.2);
        Assertions.assertEquals(6, mathFunctionManual.getCount());
        mathFunctionManual.insert(15.5, 20.7);
        Assertions.assertEquals(7, mathFunctionManual.getCount());
        Assertions.assertEquals(2, mathFunctionManual.emptyPlaces);

        Assertions.assertEquals(0.6, mathFunctionManual.getX(0));
        Assertions.assertEquals(2, mathFunctionManual.indexOfY(5.6));
        Assertions.assertEquals(20.7, mathFunctionManual.getY(6));

        mathFunctionManual.insert(1.9, 0.7);
        Assertions.assertEquals(2, mathFunctionManual.indexOfY(0.7));

        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> mathFunctionManual.getX(10));
    }

    @Test
    void applyTest() {
        Assertions.assertEquals(2.26, mathFunctionDiscrete.apply(1.5), EPSILON);
        Assertions.assertEquals(1.96, mathFunctionDiscrete.apply(1.4), EPSILON);
        Assertions.assertEquals(7.8, mathFunctionDiscrete.apply(3.0), EPSILON);
        Assertions.assertEquals(-1.2, mathFunctionDiscrete.apply(0), EPSILON);

        mathFunctionDiscrete = new ArrayTabulatedFunction(new DerivativeFunction(new NRootCalculateFunction(5)), 10, 25, 20);

        Assertions.assertEquals(0.02982, mathFunctionDiscrete.apply(10.7894), EPSILON);
        Assertions.assertEquals(0.03050, mathFunctionDiscrete.apply(10.5), EPSILON);
        Assertions.assertEquals(0.01268, mathFunctionDiscrete.apply(30.0), EPSILON);
        Assertions.assertEquals(0.04353, mathFunctionDiscrete.apply(5.0), EPSILON);

        Assertions.assertEquals(3.4, mathFunctionManual.apply(2.5), EPSILON);
        Assertions.assertEquals(2.95, mathFunctionManual.apply(3.0), EPSILON);
        Assertions.assertEquals(0.06666, mathFunctionManual.apply(0.0), EPSILON);
        Assertions.assertEquals(3.366, mathFunctionManual.apply(10.0), EPSILON);
    }

    @Test
    void getterTest() {
        Assertions.assertEquals(1.2, mathFunctionDiscrete.getX(1), EPSILON);
        Assertions.assertEquals(2.0, mathFunctionDiscrete.getX(5), EPSILON);
        Assertions.assertEquals(4.0, mathFunctionDiscrete.getY(5), EPSILON);
        Assertions.assertEquals(1.96, mathFunctionDiscrete.getY(2), EPSILON);
        Assertions.assertEquals(5.0, mathFunctionManual.getX(3), EPSILON);
        Assertions.assertEquals(1.0, mathFunctionManual.getX(0), EPSILON);
        Assertions.assertEquals(3.4, mathFunctionManual.getY(1), EPSILON);
        Assertions.assertEquals(2.5, mathFunctionManual.getY(2), EPSILON);

        Assertions.assertEquals(1.0, mathFunctionDiscrete.leftBound(), EPSILON);
        Assertions.assertEquals(2.0, mathFunctionDiscrete.rightBound(), EPSILON);
        Assertions.assertEquals(1.0, mathFunctionManual.leftBound(), EPSILON);
        Assertions.assertEquals(5.0, mathFunctionManual.rightBound(), EPSILON);

        Assertions.assertEquals(2, mathFunctionDiscrete.indexOfX(1.4));
        Assertions.assertEquals(-1, mathFunctionDiscrete.indexOfX(3.0));
        Assertions.assertEquals(5, mathFunctionDiscrete.indexOfY(4.0));
        Assertions.assertEquals(-1, mathFunctionDiscrete.indexOfY(9.0));
        Assertions.assertEquals(1, mathFunctionManual.indexOfX(2.5));
        Assertions.assertEquals(-1, mathFunctionManual.indexOfX(9.0));
        Assertions.assertEquals(1, mathFunctionManual.indexOfY(3.4));
        Assertions.assertEquals(-1, mathFunctionManual.indexOfY(2.4));

        Assertions.assertEquals(6, mathFunctionDiscrete.getCount());
        Assertions.assertEquals(4, mathFunctionManual.getCount());

        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> mathFunctionDiscrete.getX(10));
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> mathFunctionDiscrete.getY(-1));
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> mathFunctionDiscrete.getX(-1));
    }

    @Test
    void setterTest() {
        Assertions.assertDoesNotThrow(() -> mathFunctionDiscrete.setY(3, 1.5));
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> mathFunctionDiscrete.setY(10, 1.5));

        Assertions.assertDoesNotThrow(() -> mathFunctionManual.setY(2, 1.0));
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> mathFunctionManual.setY(4, 1.5));

        mathFunctionDiscrete.setY(2, 4.0);
        Assertions.assertEquals(4.0, mathFunctionDiscrete.getY(2));

        mathFunctionDiscrete.setY(3, 1.0);
        Assertions.assertEquals(1.0, mathFunctionDiscrete.getY(3));

        mathFunctionManual.setY(3, 2.5);
        Assertions.assertEquals(2.5, mathFunctionManual.getY(3));

        mathFunctionManual.setY(0, 2.0);
        Assertions.assertEquals(2.0, mathFunctionManual.getY(0));
    }

}
