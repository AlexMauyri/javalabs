package ru.ssau.tk.DoubleA.javalabs.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SqrFunctionTest extends AbstractTest {

    @Test
    void test() {
        SqrFunction sqrFunction = new SqrFunction();
        Assertions.assertEquals(100, sqrFunction.apply(10));
        Assertions.assertEquals(1, sqrFunction.apply(1));
        Assertions.assertEquals(100, sqrFunction.apply(-10));
        Assertions.assertEquals(0, sqrFunction.apply(0));
    }


}
