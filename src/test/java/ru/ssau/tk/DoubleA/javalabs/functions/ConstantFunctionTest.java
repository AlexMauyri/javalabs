package ru.ssau.tk.DoubleA.javalabs.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConstantFunctionTest extends AbstractTest {
    ConstantFunction constFunction = new ConstantFunction(100);

    @Test
    void test() {
        Assertions.assertEquals(constFunction.getCONSTANT(), constFunction.apply(1), EPSILON);
        Assertions.assertEquals(constFunction.getCONSTANT(), constFunction.apply(-1), EPSILON);
        Assertions.assertEquals(constFunction.getCONSTANT(), constFunction.apply(0), EPSILON);
    }
}
