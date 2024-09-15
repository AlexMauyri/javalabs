package ru.ssau.tk.DoubleA.javalabs.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CompositeFunctionTest extends AbstractTest {

    @Test
    void test() {
        CompositeFunction function = new CompositeFunction(new IdentityFunction(), new SqrFunction());
        Assertions.assertEquals(121, function.apply(11));
        function = new CompositeFunction(function, function);
        Assertions.assertEquals(38416, function.apply(14));
        function = new CompositeFunction(new NRootCalculateFunction(2), new SqrFunction());
        Assertions.assertTrue(Math.abs(function.apply(25)) - 25 <= EPSILON);
    }
}