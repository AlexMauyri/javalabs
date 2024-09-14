package ru.ssau.tk.DoubleA.javalabs.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UnitFunctionTest {
    @Test
    void test() {
        UnitFunction unitFunction = new UnitFunction();
        Assertions.assertEquals(unitFunction.getCONSTANT(), unitFunction.apply(1));
        Assertions.assertEquals(unitFunction.getCONSTANT(), unitFunction.apply(-1));
        Assertions.assertEquals(unitFunction.getCONSTANT(), unitFunction.apply(0));
    }
}
