package ru.ssau.tk.DoubleA.javalabs.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SqrFunctionTest extends AbstractTest {
    SqrFunction sqrFunction;

    @Test
    void test() {
        sqrFunction = new SqrFunction();
        Assertions.assertEquals(100, sqrFunction.apply(10));
        Assertions.assertEquals(1, sqrFunction.apply(1));
        Assertions.assertEquals(100, sqrFunction.apply(-10));
        Assertions.assertEquals(0, sqrFunction.apply(0));
        Assertions.assertEquals(1002.735556, sqrFunction.apply(-31.666), EPSILON);
        Assertions.assertEquals(548.636929, sqrFunction.apply(23.423), EPSILON);
    }


}
