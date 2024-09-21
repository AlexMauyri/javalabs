package ru.ssau.tk.DoubleA.javalabs.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class StrictTabulatedFunctionTest {
    TabulatedFunction manualFunction, discreteFunction;

    @BeforeEach
    void init() {
        manualFunction = new LinkedListTabulatedFunction(new double[]{-3, 1.5, 6, 10.5, 15}, new double[]{9, 2.25, 36, 110.25, 225});
        discreteFunction = new LinkedListTabulatedFunction(new SqrFunction(), -3, 15, 5);
    }

    @Test
    void testListTabulatedStrict() {
        TabulatedFunction strictManualList = new StrictTabulatedFunction(manualFunction);
        TabulatedFunction strictDiscreteList = new StrictTabulatedFunction(discreteFunction);

        Assertions.assertEquals(5, strictManualList.getCount());
        Assertions.assertEquals(10.5, strictDiscreteList.getX(3));
        Assertions.assertEquals(225, strictManualList.getY(4));
        Assertions.assertEquals(1, strictDiscreteList.indexOfX(1.5));
        Assertions.assertEquals(0, strictManualList.indexOfY(9));
        Assertions.assertEquals(-3, strictDiscreteList.leftBound());
        Assertions.assertEquals(15, strictManualList.rightBound());

        Iterator<Point> iterator = strictDiscreteList.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            Point point = iterator.next();
            Assertions.assertEquals(discreteFunction.getX(i++), point.x);
        }
        Assertions.assertThrows(NoSuchElementException.class, iterator::next);

        i = 0;
        for (Point point : strictManualList) {
            Assertions.assertEquals(manualFunction.getX(i), point.x);
            ++i;
        }

        Assertions.assertEquals(2.25, strictManualList.apply(1.5));
        Assertions.assertEquals(110.25, strictDiscreteList.apply(10.5));
        Assertions.assertThrows(UnsupportedOperationException.class, () -> strictManualList.apply(7));
        Assertions.assertThrows(UnsupportedOperationException.class, () -> strictDiscreteList.apply(-10));
    }

    @Test
    void testArrayTabulatedStrict() {
        StrictTabulatedFunction strictManualArray = new StrictTabulatedFunction(manualFunction);
        StrictTabulatedFunction strictDiscreteArray = new StrictTabulatedFunction(discreteFunction);

        Assertions.assertEquals(5, strictManualArray.getCount());
        Assertions.assertEquals(15, strictDiscreteArray.getX(4));
        Assertions.assertEquals(225, strictManualArray.getY(4));
        Assertions.assertEquals(1, strictDiscreteArray.indexOfX(1.5));
        Assertions.assertEquals(1, strictManualArray.indexOfY(2.25));
        Assertions.assertEquals(-3, strictDiscreteArray.leftBound());
        Assertions.assertEquals(15, strictManualArray.rightBound());

        Iterator<Point> iterator = strictManualArray.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            Point point = iterator.next();
            Assertions.assertEquals(discreteFunction.getX(i++), point.x);
        }
        Assertions.assertThrows(NoSuchElementException.class, iterator::next);

        i = 0;
        for (Point point : strictDiscreteArray) {
            Assertions.assertEquals(manualFunction.getX(i), point.x);
            ++i;
        }

        Assertions.assertEquals(2.25, strictManualArray.apply(1.5));
        Assertions.assertEquals(110.25, strictDiscreteArray.apply(10.5));
        Assertions.assertThrows(UnsupportedOperationException.class, () -> strictManualArray.apply(7));
        Assertions.assertThrows(UnsupportedOperationException.class, () -> strictDiscreteArray.apply(-10));
    }
}
