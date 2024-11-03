package ru.ssau.tk.DoubleA.javalabs.concurrent;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ssau.tk.DoubleA.javalabs.functions.*;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SynchronizedTabulatedFunctionTest extends AbstractTest {

    SynchronizedTabulatedFunction function1;
    SynchronizedTabulatedFunction function2;

    @BeforeEach
    void init() {
        ArrayTabulatedFunction mathFunctionManual = new ArrayTabulatedFunction(
                new double[]{1.0, 2.5, 3.5, 5.0},
                new double[]{1.4, 3.4, 2.5, 2.7}
        );
        LinkedListTabulatedFunction manualList = new LinkedListTabulatedFunction(
                new double[]{-3, 1.5, 6, 10.5, 15},
                new double[]{9, 2.25, 36, 110.25, 225}
        );
        function1 = new SynchronizedTabulatedFunction(mathFunctionManual);
        function2 = new SynchronizedTabulatedFunction(manualList);
    }

    @Test
    void testSynchronizedGetters() {
        Assertions.assertEquals(5.0, function1.getX(3), EPSILON);
        Assertions.assertEquals(1.0, function1.getX(0), EPSILON);
        Assertions.assertEquals(3.4, function1.getY(1), EPSILON);
        Assertions.assertEquals(2.5, function1.getY(2), EPSILON);
        Assertions.assertEquals(6, function2.getX(2));
        Assertions.assertEquals(2.25, function2.getY(1));

        Assertions.assertEquals(1.0, function1.leftBound(), EPSILON);
        Assertions.assertEquals(5.0, function1.rightBound(), EPSILON);
        Assertions.assertEquals(-3, function2.leftBound());
        Assertions.assertEquals(15, function2.rightBound());

        Assertions.assertEquals(1, function1.indexOfX(2.5));
        Assertions.assertEquals(-1, function1.indexOfX(9.0));
        Assertions.assertEquals(1, function1.indexOfY(3.4));
        Assertions.assertEquals(-1, function1.indexOfY(2.4));
        Assertions.assertEquals(4, function2.indexOfX(15));
        Assertions.assertEquals(-1, function2.indexOfX(20));
        Assertions.assertEquals(3, function2.indexOfY(110.25));

        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> function1.getX(-1));
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> function1.getX(4));
        Assertions.assertThrows(IllegalArgumentException.class, () -> function2.getX(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> function2.getX(5));

        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> function1.getY(-1));
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> function1.getY(4));
        Assertions.assertThrows(IllegalArgumentException.class, () -> function2.getY(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> function2.getY(5));

        Assertions.assertEquals(new ArrayTabulatedFunction(new double[]{1.0, 2.5, 3.5, 5.0},new double[]{1.4, 3.4, 2.5, 2.7}).hashCode(), function1.hashCode());
    }

    @Test
    void testSynchronizedSetters() {
        Assertions.assertDoesNotThrow(() -> function1.setY(2, 1.0));
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> function1.setY(4, 1.5));

        function1.setY(3, 2.5);
        Assertions.assertEquals(2.5, function1.getY(3));

        function1.setY(0, 2.0);
        Assertions.assertEquals(2.0, function1.getY(0));

        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> function1.setY(-1, 0));
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> function1.setY(4, 0));

        function2.setY(3, 169);
        Assertions.assertEquals(169, function2.getY(3));
        Assertions.assertThrows(IllegalArgumentException.class, () -> function2.setY(-1, 0));
        Assertions.assertThrows(IllegalArgumentException.class, () -> function2.setY(5, 0));
    }

    @Test
    void testSynchronizedIterator() {
        Iterator<Point> iterator = function1.iterator();
        int i = 0;

        while (iterator.hasNext()) {
            Point point = iterator.next();
            Assertions.assertEquals(function1.getX(i), point.x, EPSILON);
            i += 1;
        }

        Assertions.assertThrows(NoSuchElementException.class, iterator::next);

        i = 0;
        for (Point point : function1) {
            Assertions.assertEquals(function1.getX(i), point.x);
            ++i;
        }

        Iterator<Point> iterator2 = function2.iterator();

        i = 0;
        while (iterator2.hasNext()) {
            Point point = iterator2.next();
            Assertions.assertEquals(function2.getX(i++), point.x);
        }
        Assertions.assertThrows(NoSuchElementException.class, iterator2::next);

        i = 0;
        for (Point point : function2) {
            Assertions.assertEquals(function2.getX(i++), point.x);
        }
    }

    @Test
    void testSynchronizedApply() {
        Assertions.assertEquals(3.4, function1.apply(2.5), EPSILON);
        Assertions.assertEquals(2.95, function1.apply(3.0), EPSILON);
        Assertions.assertEquals(0.06666, function1.apply(0.0), EPSILON);
        Assertions.assertEquals(3.366, function1.apply(10.0), EPSILON);
        Assertions.assertEquals(13.5, function2.apply(-6));
        Assertions.assertEquals(352.5, function2.apply(20));
        Assertions.assertEquals(2.25, function2.apply(1.5));
        Assertions.assertEquals(85.5, function2.apply(9));
    }

    @Test
    void testDoSynchronously() {
        SynchronizedTabulatedFunction function = new SynchronizedTabulatedFunction(
                new ArrayTabulatedFunction(
                        new SqrFunction(), 3, 100, 17
                )
        );

        Assertions.assertInstanceOf(SynchronizedTabulatedFunction.class,
                function.doSynchronously(
                        function13 -> {
                            Assertions.assertEquals(17, function13.getCount());
                            Assertions.assertEquals(3.0, function13.leftBound());
                            Assertions.assertEquals(100, function13.rightBound());
                            Assertions.assertEquals(27.25, function13.getX(4), EPSILON);
                            Assertions.assertEquals(51.5, function13.getX(8), EPSILON);
                            function13.setY(3, 3.5);
                            function13.setY(10, 100.0);
                            function13.setY(1, 1.5);
                            return function13;
                        }
                )
        );

        Assertions.assertInstanceOf(ArrayTabulatedFunction.class,
                function.doSynchronously(
                        function1 -> {
                            Assertions.assertEquals(1.5, function1.getY(1), EPSILON);
                            Assertions.assertEquals(3.5, function1.getY(3), EPSILON);
                            Assertions.assertEquals(100.0, function1.getY(10), EPSILON);
                            Assertions.assertEquals(3, function1.indexOfY(3.5));
                            Assertions.assertEquals(8, function1.indexOfY(2652.25));
                            Assertions.assertEquals(4, function1.indexOfY(742.5625));
                            return (ArrayTabulatedFunction) function1.tabulatedFunction;
                        }
                )
        );


        Assertions.assertNull(function.doSynchronously(
                function12 -> {
                    function12.setY(5, 9.5);
                    function12.setY(15, -20.0);
                    function12.setY(4, 13.5);
                    Assertions.assertEquals(9.5, function12.getY(5), EPSILON);
                    Assertions.assertEquals(-20.0, function12.getY(15), EPSILON);
                    Assertions.assertEquals(13.5, function12.getY(4), EPSILON);
                    return null;
                }
        ));
    }

}
