package ru.ssau.tk.DoubleA.javalabs.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IdentityFunctionTest extends AbstractTest {

    @Test
    void test() {
        IdentityFunction function = new IdentityFunction();
        Assertions.assertEquals(Math.PI, function.apply(Math.PI), EPSILON);
        Assertions.assertEquals(-214768000.000867412, function.apply(-214768000.000867412), EPSILON);
        Assertions.assertEquals(365, function.apply(365), EPSILON);
    }
}
