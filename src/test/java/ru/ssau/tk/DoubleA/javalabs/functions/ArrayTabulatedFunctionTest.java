package ru.ssau.tk.DoubleA.javalabs.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ssau.tk.DoubleA.javalabs.exceptions.*;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayTabulatedFunctionTest extends AbstractTest {
    ArrayTabulatedFunction mathFunctionDiscrete;
    ArrayTabulatedFunction mathFunctionManual;

    @Test
    void testToString() {
        ArrayTabulatedFunction mathFunctionManual = new ArrayTabulatedFunction(new double[]{9, 11}, new double[]{18, 25});
        ArrayTabulatedFunction mathFunctionDiscrete = new ArrayTabulatedFunction(new SqrFunction(), -3, 15, 5);

        Assertions.assertEquals("ArrayTabulatedFunction size = 2\n[9.0; 18.0]\n[11.0; 25.0]", mathFunctionManual.toString());
        Assertions.assertEquals("ArrayTabulatedFunction size = 5\n[-3.0; 9.0]\n[1.5; 2.25]\n[6.0; 36.0]\n[10.5; 110.25]\n[15.0; 225.0]", mathFunctionDiscrete.toString());
    }

    @Test
    void constructorTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ArrayTabulatedFunction(new double[]{1.0}, new double[]{1.4}));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ArrayTabulatedFunction(new double[]{1.0}, new double[]{}));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ArrayTabulatedFunction(new double[]{1.0, 2.0}, new double[]{}));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ArrayTabulatedFunction(new double[]{1.0}, new double[]{1.0, 2.0}));

        Assertions.assertThrows(DifferentLengthOfArraysException.class, () -> new ArrayTabulatedFunction(new double[]{1.0, 2.0, 3.0}, new double[]{1.4, 2.8}));
        Assertions.assertThrows(DifferentLengthOfArraysException.class, () -> new ArrayTabulatedFunction(new double[]{1.0, 2.0}, new double[]{1.4, 2.8, 4.2}));

        Assertions.assertThrows(ArrayIsNotSortedException.class, () -> new ArrayTabulatedFunction(new double[]{1.0, 3.0, 2.0}, new double[]{1.4, 2.8, 4.2}));
        Assertions.assertThrows(ArrayIsNotSortedException.class, () -> new ArrayTabulatedFunction(new double[]{1.0, 2.0, -3.0}, new double[]{1.4, 2.8, 4.2}));

        Assertions.assertThrows(IllegalArgumentException.class, () -> new ArrayTabulatedFunction(new SqrFunction(), 1.0, 2.0, 1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ArrayTabulatedFunction(new SqrFunction(), 5.0, 5.0, 1));
    }

    @BeforeEach
    void init() {
        mathFunctionDiscrete = new ArrayTabulatedFunction(new SqrFunction(), 1.0, 2.0, 6);
        mathFunctionManual = new ArrayTabulatedFunction(
                new double[]{1.0, 2.5, 3.5, 5.0},
                new double[]{1.4, 3.4, 2.5, 2.7}
        );
    }

    @Test
    void removeTest() {
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> mathFunctionManual.remove(-2));
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> mathFunctionManual.remove(6));
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> mathFunctionDiscrete.remove(-2));
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> mathFunctionDiscrete.remove(6));

        mathFunctionManual.remove(3);
        Assertions.assertEquals(3, mathFunctionManual.getCount());

        mathFunctionManual.remove(0);
        Assertions.assertEquals(2, mathFunctionManual.getCount());

        Assertions.assertThrows(IllegalStateException.class, () -> mathFunctionManual.remove(1));
        Assertions.assertThrows(IllegalStateException.class, () -> mathFunctionManual.remove(0));

        mathFunctionManual = new ArrayTabulatedFunction(
                new double[]{1.0, 2.5, 3.5, 5.0, 7.0, 9.0, 10.0},
                new double[]{1.4, 3.4, 2.5, 2.7, 3.4, 5.4, 6.5}
        );
        mathFunctionManual.remove(0);
        mathFunctionManual.remove(0);
        mathFunctionManual.remove(0);
        mathFunctionManual.remove(0);
        mathFunctionManual.remove(0);
        Assertions.assertEquals(2, mathFunctionManual.getCount());
    }

    @Test
    void insertTest() {
        mathFunctionManual.insert(1.9, 5.6);
        Assertions.assertEquals(5, mathFunctionManual.getCount());

        mathFunctionManual.insert(0.6, -3.2);
        Assertions.assertEquals(6, mathFunctionManual.getCount());

        mathFunctionManual.insert(15.5, 20.7);
        Assertions.assertEquals(7, mathFunctionManual.getCount());

        Assertions.assertEquals(0.6, mathFunctionManual.getX(0));
        Assertions.assertEquals(2, mathFunctionManual.indexOfY(5.6));
        Assertions.assertEquals(20.7, mathFunctionManual.getY(6));

        mathFunctionManual.insert(1.9, 0.7);
        Assertions.assertEquals(2, mathFunctionManual.indexOfY(0.7));
    }

    @Test
    void applyTest() {
        Assertions.assertEquals(2.26, mathFunctionDiscrete.apply(1.5), EPSILON);
        Assertions.assertEquals(1.96, mathFunctionDiscrete.apply(1.4), EPSILON);
        Assertions.assertEquals(7.8, mathFunctionDiscrete.apply(3.0), EPSILON);
        Assertions.assertEquals(-1.2, mathFunctionDiscrete.apply(0), EPSILON);

        mathFunctionDiscrete = new ArrayTabulatedFunction(
                new DerivativeFunction(
                        new NRootCalculateFunction(5)
                ), 25, 10, 20
        );

        Assertions.assertEquals(0.02982, mathFunctionDiscrete.apply(10.7894), EPSILON);
        Assertions.assertEquals(0.03050, mathFunctionDiscrete.apply(10.5), EPSILON);
        Assertions.assertEquals(0.01268, mathFunctionDiscrete.apply(30.0), EPSILON);
        Assertions.assertEquals(0.04353, mathFunctionDiscrete.apply(5.0), EPSILON);

        Assertions.assertEquals(3.4, mathFunctionManual.apply(2.5), EPSILON);
        Assertions.assertEquals(2.95, mathFunctionManual.apply(3.0), EPSILON);
        Assertions.assertEquals(0.06666, mathFunctionManual.apply(0.0), EPSILON);
        Assertions.assertEquals(3.366, mathFunctionManual.apply(10.0), EPSILON);

        mathFunctionDiscrete = new ArrayTabulatedFunction(
                new SqrFunction(),
                1.0,
                1.0,
                5
        );
        Assertions.assertEquals(1.0, mathFunctionDiscrete.apply(1.5), EPSILON);

        mathFunctionDiscrete = new ArrayTabulatedFunction(
                new SqrFunction(),
                1.0,
                5.0,
                5
        );
        Assertions.assertEquals(2.5, mathFunctionDiscrete.apply(1.5), EPSILON);
    }

    @Test
    void getterTest() {
        Assertions.assertEquals(1, mathFunctionManual.floorIndexOfX(2.5), EPSILON);
        Assertions.assertEquals(0, mathFunctionManual.floorIndexOfX(2), EPSILON);
        Assertions.assertEquals(3, mathFunctionManual.floorIndexOfX(5), EPSILON);
        Assertions.assertEquals(3, mathFunctionDiscrete.floorIndexOfX(1.6), EPSILON);
        Assertions.assertEquals(2, mathFunctionDiscrete.floorIndexOfX(1.5), EPSILON);
        Assertions.assertEquals(6, mathFunctionDiscrete.floorIndexOfX(3), EPSILON);

        Assertions.assertThrows(IllegalArgumentException.class, () -> mathFunctionManual.floorIndexOfX(0.5));
        Assertions.assertThrows(IllegalArgumentException.class, () -> mathFunctionManual.floorIndexOfX(-127));
        Assertions.assertThrows(IllegalArgumentException.class, () -> mathFunctionManual.floorIndexOfX(0.9));
        Assertions.assertThrows(IllegalArgumentException.class, () -> mathFunctionDiscrete.floorIndexOfX(0.5));
        Assertions.assertThrows(IllegalArgumentException.class, () -> mathFunctionDiscrete.floorIndexOfX(-127));
        Assertions.assertThrows(IllegalArgumentException.class, () -> mathFunctionDiscrete.floorIndexOfX(0.9));

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

        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> mathFunctionManual.getX(-1));
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> mathFunctionManual.getX(4));
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> mathFunctionDiscrete.getX(-1));
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> mathFunctionDiscrete.getX(6));

        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> mathFunctionManual.getY(-1));
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> mathFunctionManual.getY(4));
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> mathFunctionDiscrete.getY(-1));
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> mathFunctionDiscrete.getY(6));

        Assertions.assertThrows(IllegalArgumentException.class, () -> mathFunctionManual.interpolate(10, -1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> mathFunctionManual.interpolate(5, 3));
        Assertions.assertThrows(InterpolationException.class, () -> mathFunctionManual.interpolate(21.7, 0));
        Assertions.assertThrows(IllegalArgumentException.class, () -> mathFunctionDiscrete.interpolate(10, -1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> mathFunctionDiscrete.interpolate(5, 5));
        Assertions.assertThrows(InterpolationException.class, () -> mathFunctionDiscrete.interpolate(4.36, 1));
        Assertions.assertEquals("Hello", new InterpolationException("Hello").getMessage());
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

        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> mathFunctionManual.setY(-1, 0));
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> mathFunctionManual.setY(4, 0));
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> mathFunctionDiscrete.setY(-1, 0));
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> mathFunctionDiscrete.setY(6, 0));
    }

    @Test
    void iteratorTest() {
        Iterator<Point> iterator1 = mathFunctionDiscrete.iterator();
        Iterator<Point> iterator2 = mathFunctionManual.iterator();
        int i = 0;
        while (iterator1.hasNext()) {
            Point point = iterator1.next();
            Assertions.assertEquals(mathFunctionDiscrete.getX(i), point.x, EPSILON);
            i += 1;
        }

        i = 0;
        while (iterator2.hasNext()) {
            Point point = iterator2.next();
            Assertions.assertEquals(mathFunctionManual.getX(i), point.x, EPSILON);
            i += 1;
        }

        Assertions.assertThrows(NoSuchElementException.class, iterator1::next);
        Assertions.assertThrows(NoSuchElementException.class, iterator2::next);

        i = 0;
        for (Point point : mathFunctionManual) {
            Assertions.assertEquals(mathFunctionManual.getX(i), point.x);
            ++i;
        }

        i = 0;
        for (Point point : mathFunctionDiscrete) {
            Assertions.assertEquals(mathFunctionDiscrete.getX(i), point.x);
            ++i;
        }
    }
}
