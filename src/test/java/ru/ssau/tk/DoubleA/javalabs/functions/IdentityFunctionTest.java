package ru.ssau.tk.DoubleA.javalabs.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IdentityFunctionTest extends AbstractTest {
    IdentityFunction identityFunction;

    @Test
    void test() {
        identityFunction = new IdentityFunction();
        Assertions.assertEquals(Math.PI, identityFunction.apply(Math.PI), EPSILON);
        Assertions.assertEquals(-214768000.000867412, identityFunction.apply(-214768000.000867412), EPSILON);
        Assertions.assertEquals(365, identityFunction.apply(365), EPSILON);
    }
}
