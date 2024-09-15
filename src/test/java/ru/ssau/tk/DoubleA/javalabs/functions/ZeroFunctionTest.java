package ru.ssau.tk.DoubleA.javalabs.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ZeroFunctionTest extends AbstractTest {
    @Test
    void test() {
        ZeroFunction zeroFunction = new ZeroFunction();
        Assertions.assertEquals(zeroFunction.getCONSTANT(), zeroFunction.apply(1));
        Assertions.assertEquals(zeroFunction.getCONSTANT(), zeroFunction.apply(-1));
        Assertions.assertEquals(zeroFunction.getCONSTANT(), zeroFunction.apply(0));
    }
}
