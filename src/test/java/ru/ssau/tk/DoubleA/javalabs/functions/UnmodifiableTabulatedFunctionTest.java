package ru.ssau.tk.DoubleA.javalabs.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ssau.tk.DoubleA.javalabs.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.DoubleA.javalabs.functions.factory.LinkedListTabulatedFunctionFactory;
import ru.ssau.tk.DoubleA.javalabs.functions.factory.TabulatedFunctionFactory;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class UnmodifiableTabulatedFunctionTest extends AbstractTest {

    TabulatedFunction tabulatedFunction1;
    TabulatedFunction tabulatedFunction2;

    StrictTabulatedFunction tabulatedFunction;

    @BeforeEach
    void init() {
        tabulatedFunction1 = new UnmodifiableTabulatedFunction(
                new ArrayTabulatedFunction(
                    new double[]{1.0, 2.5, 3.5, 5.0},
                    new double[]{1.4, 3.4, 2.5, 2.7}
                )
        );
        tabulatedFunction2 = new UnmodifiableTabulatedFunction(
                new LinkedListTabulatedFunction(
                    new double[]{-3, 1.5, 6, 10.5, 15},
                    new double[]{9, 2.25, 36, 110.25, 225}
                )
        );
    }

    @Test
    void createUnmodifiableTest() {
        TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();
        TabulatedFunction function = factory.createUnmodifiable(
                new double[]{1.0, 2.5, 3.5, 5.0},
                new double[]{1.4, 3.4, 2.5, 2.7}
        );

        Assertions.assertEquals(3.4, function.apply(2.5), EPSILON);
        Assertions.assertEquals(2.95, function.apply(3.0), EPSILON);
        Assertions.assertEquals(0.06666, function.apply(0.0), EPSILON);
        Assertions.assertEquals(3.366, function.apply(10.0), EPSILON);

        Assertions.assertEquals(5.0, function.getX(3), EPSILON);
        Assertions.assertEquals(1.0, function.getX(0), EPSILON);
        Assertions.assertEquals(3.4, function.getY(1), EPSILON);
        Assertions.assertEquals(2.5, function.getY(2), EPSILON);

        Assertions.assertEquals(1.0, function.leftBound(), EPSILON);
        Assertions.assertEquals(5.0, function.rightBound(), EPSILON);

        Assertions.assertEquals(1, function.indexOfX(2.5));
        Assertions.assertEquals(-1, function.indexOfX(9.0));
        Assertions.assertEquals(1, function.indexOfY(3.4));
        Assertions.assertEquals(-1, function.indexOfY(2.4));

        Assertions.assertEquals(4, function.getCount());

        Iterator<Point> iterator = function.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            Point point = iterator.next();
            Assertions.assertEquals(function.getX(i), point.x, EPSILON);
            i += 1;
        }

        Assertions.assertThrows(NoSuchElementException.class, iterator::next);

        i = 0;
        for (Point point : function) {
            Assertions.assertEquals(function.getX(i), point.x);
            ++i;
        }

        Assertions.assertThrows(UnsupportedOperationException.class, () -> function.setY(2, 10));

        factory = new LinkedListTabulatedFunctionFactory();
        TabulatedFunction function2 = factory.createUnmodifiable(
                new double[]{-3, 1.5, 6, 10.5, 15},
                new double[]{9, 2.25, 36, 110.25, 225}
        );

        Assertions.assertEquals(13.5, function2.apply(-6));
        Assertions.assertEquals(352.5, function2.apply(20));
        Assertions.assertEquals(2.25, function2.apply(1.5));
        Assertions.assertEquals(85.5, function2.apply(9));

        Assertions.assertEquals(6, function2.getX(2));
        Assertions.assertEquals(2.25, function2.getY(1));


        Assertions.assertEquals(-3, function2.leftBound());
        Assertions.assertEquals(15, function2.rightBound());

        Assertions.assertEquals(5, function2.getCount());


        Assertions.assertThrows(UnsupportedOperationException.class, () -> function2.setY(3, 5));
    }

    @Test
    void unmodifiableAndStrictTest() {
        tabulatedFunction = new StrictTabulatedFunction(tabulatedFunction1);

        Assertions.assertEquals(tabulatedFunction1.hashCode(), tabulatedFunction.hashCode());

        Assertions.assertDoesNotThrow(() -> tabulatedFunction.apply(2.5));
        Assertions.assertThrows(UnsupportedOperationException.class, () -> tabulatedFunction.apply(2.4));
        Assertions.assertDoesNotThrow(() -> tabulatedFunction.apply(3.5));
        Assertions.assertThrows(UnsupportedOperationException.class, () -> tabulatedFunction.apply(4.0));
        Assertions.assertDoesNotThrow(() -> tabulatedFunction.apply(5.0));
        Assertions.assertThrows(UnsupportedOperationException.class, () -> tabulatedFunction.apply(6.0));
        Assertions.assertDoesNotThrow(() -> tabulatedFunction.apply(1.0));
        Assertions.assertThrows(UnsupportedOperationException.class, () -> tabulatedFunction.apply(0.0));

        Assertions.assertThrows(UnsupportedOperationException.class, () -> tabulatedFunction.setY(2, 10));

        Assertions.assertEquals(5.0, tabulatedFunction.getX(3), EPSILON);
        Assertions.assertEquals(1.0, tabulatedFunction.getX(0), EPSILON);
        Assertions.assertEquals(3.4, tabulatedFunction.getY(1), EPSILON);
        Assertions.assertEquals(2.5, tabulatedFunction.getY(2), EPSILON);

        Assertions.assertEquals(1.0, tabulatedFunction.leftBound(), EPSILON);
        Assertions.assertEquals(5.0, tabulatedFunction.rightBound(), EPSILON);

        Assertions.assertEquals(1, tabulatedFunction.indexOfX(2.5));
        Assertions.assertEquals(-1, tabulatedFunction.indexOfX(9.0));
        Assertions.assertEquals(1, tabulatedFunction.indexOfY(3.4));
        Assertions.assertEquals(-1, tabulatedFunction.indexOfY(2.4));

        Assertions.assertEquals(4, tabulatedFunction.getCount());

        Iterator<Point> iterator = tabulatedFunction.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            Point point = iterator.next();
            Assertions.assertEquals(tabulatedFunction.getX(i), point.x, EPSILON);
            i += 1;
        }

        Assertions.assertThrows(NoSuchElementException.class, iterator::next);

        i = 0;
        for (Point point : tabulatedFunction) {
            Assertions.assertEquals(tabulatedFunction.getX(i), point.x);
            ++i;
        }
    }

    @Test
    void applyTest() {
        Assertions.assertEquals(3.4, tabulatedFunction1.apply(2.5), EPSILON);
        Assertions.assertEquals(2.95, tabulatedFunction1.apply(3.0), EPSILON);
        Assertions.assertEquals(0.06666, tabulatedFunction1.apply(0.0), EPSILON);
        Assertions.assertEquals(3.366, tabulatedFunction1.apply(10.0), EPSILON);
        Assertions.assertEquals(13.5, tabulatedFunction2.apply(-6));
        Assertions.assertEquals(352.5, tabulatedFunction2.apply(20));
        Assertions.assertEquals(2.25, tabulatedFunction2.apply(1.5));
        Assertions.assertEquals(85.5, tabulatedFunction2.apply(9));
    }

    @Test
    void getterTest() {
        Assertions.assertEquals(5.0, tabulatedFunction1.getX(3), EPSILON);
        Assertions.assertEquals(1.0, tabulatedFunction1.getX(0), EPSILON);
        Assertions.assertEquals(3.4, tabulatedFunction1.getY(1), EPSILON);
        Assertions.assertEquals(2.5, tabulatedFunction1.getY(2), EPSILON);
        Assertions.assertEquals(6, tabulatedFunction2.getX(2));
        Assertions.assertEquals(2.25, tabulatedFunction2.getY(1));

        Assertions.assertEquals(1.0, tabulatedFunction1.leftBound(), EPSILON);
        Assertions.assertEquals(5.0, tabulatedFunction1.rightBound(), EPSILON);
        Assertions.assertEquals(-3, tabulatedFunction2.leftBound());
        Assertions.assertEquals(15, tabulatedFunction2.rightBound());

        Assertions.assertEquals(1, tabulatedFunction1.indexOfX(2.5));
        Assertions.assertEquals(-1, tabulatedFunction1.indexOfX(9.0));
        Assertions.assertEquals(1, tabulatedFunction1.indexOfY(3.4));
        Assertions.assertEquals(-1, tabulatedFunction1.indexOfY(2.4));

        Assertions.assertEquals(4, tabulatedFunction1.getCount());
        Assertions.assertEquals(5, tabulatedFunction2.getCount());
    }

    @Test
    void iteratorTest() {
        Iterator<Point> iterator1 = tabulatedFunction1.iterator();
        int i = 0;
        while (iterator1.hasNext()) {
            Point point = iterator1.next();
            Assertions.assertEquals(tabulatedFunction1.getX(i), point.x, EPSILON);
            i += 1;
        }

        Assertions.assertThrows(NoSuchElementException.class, iterator1::next);

        i = 0;
        for (Point point : tabulatedFunction1) {
            Assertions.assertEquals(tabulatedFunction1.getX(i), point.x);
            ++i;
        }
    }

    @Test
    void setYTest() {
        Assertions.assertThrows(UnsupportedOperationException.class, () -> tabulatedFunction1.setY(2, 10));
        Assertions.assertThrows(UnsupportedOperationException.class, () -> tabulatedFunction2.setY(3, 5));
    }
}
