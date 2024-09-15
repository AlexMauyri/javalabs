package ru.ssau.tk.DoubleA.javalabs.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConstantFunctionTest extends AbstractTest {

    @Test
    void test() {
        ConstantFunction constantFunction = new ConstantFunction(100);
        Assertions.assertEquals(constantFunction.getCONSTANT(), constantFunction.apply(1));
        Assertions.assertEquals(constantFunction.getCONSTANT(), constantFunction.apply(-1));
        Assertions.assertEquals(constantFunction.getCONSTANT(), constantFunction.apply(0));
    }
}
