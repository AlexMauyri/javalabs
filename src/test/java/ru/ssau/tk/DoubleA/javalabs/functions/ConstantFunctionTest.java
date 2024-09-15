package ru.ssau.tk.DoubleA.javalabs.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConstantFunctionTest extends AbstractTest {

    @Test
    void test() {
        ConstantFunction mathFunction = new ConstantFunction(100);
        Assertions.assertEquals(mathFunction.getCONSTANT(), mathFunction.apply(1), EPSILON);
        Assertions.assertEquals(mathFunction.getCONSTANT(), mathFunction.apply(-1), EPSILON);
        Assertions.assertEquals(mathFunction.getCONSTANT(), mathFunction.apply(0), EPSILON);
    }
}
