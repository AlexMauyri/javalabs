package ru.ssau.tk.DoubleA.javalabs.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MockTabulatedFunctionTest extends AbstractTest {
    MockTabulatedFunction mockTabulatedFunction;

    @Test
    void test() {
        mockTabulatedFunction = new MockTabulatedFunction(4.0, 6.0, 3.5, 2.4);

        double x0 = mockTabulatedFunction.leftBound();
        double x1 = mockTabulatedFunction.rightBound();
        double y0 = mockTabulatedFunction.getY(0);
        double y1 = mockTabulatedFunction.getY(1);

        Assertions.assertEquals(2.675, mockTabulatedFunction.interpolate(5.5, x0, x1, y0, y1), EPSILON);
        Assertions.assertEquals(3.775, mockTabulatedFunction.interpolate(3.5, x0, x1, y0, y1), EPSILON);
        Assertions.assertEquals(0.75, mockTabulatedFunction.interpolate(9.0, x0, x1, y0, y1), EPSILON);
        Assertions.assertEquals(3.5, mockTabulatedFunction.apply(4.0), EPSILON);
        Assertions.assertEquals(4.6, mockTabulatedFunction.apply(2.0), EPSILON);
        Assertions.assertEquals(0.2, mockTabulatedFunction.apply(10.0), EPSILON);
        Assertions.assertEquals(2.95, mockTabulatedFunction.apply(5.0), EPSILON);
    }
}
