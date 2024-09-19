package ru.ssau.tk.DoubleA.javalabs.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ZeroFunctionTest extends AbstractTest {
    ZeroFunction zeroFunction;

    @Test
    void test() {
        zeroFunction = new ZeroFunction();
        Assertions.assertEquals(zeroFunction.getCONSTANT(), zeroFunction.apply(1));
        Assertions.assertEquals(zeroFunction.getCONSTANT(), zeroFunction.apply(-1));
        Assertions.assertEquals(zeroFunction.getCONSTANT(), zeroFunction.apply(0));
    }
}
