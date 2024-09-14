package ru.ssau.tk.DoubleA.javalabs.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CompositeFunctionTest {

    @Test
    void test() {
        CompositeFunction function = new CompositeFunction(new IdentityFunction(), new SqrFunction());
        Assertions.assertEquals(121, function.apply(11));
        CompositeFunction function1 = new CompositeFunction(function, function);
        Assertions.assertEquals(38416, function1.apply(14));

    }
}