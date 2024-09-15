package ru.ssau.tk.DoubleA.javalabs.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DerivativeFunctionTest extends AbstractTest {

    @Test
    void test() {
        DerivativeFunction derivativeFunction = new DerivativeFunction(new SqrFunction());
        Assertions.assertTrue(Math.abs(8 - derivativeFunction.apply(4)) <= EPSILON);
        derivativeFunction = new DerivativeFunction(new UnitFunction());
        Assertions.assertTrue(Math.abs(0 - derivativeFunction.apply(4)) <= EPSILON);
        derivativeFunction = new DerivativeFunction(new NRootCalculateFunction(3).andThen(new SqrFunction()));
        Assertions.assertTrue(Math.abs(0.0879857462720519825939632373 - derivativeFunction.apply(435)) <= EPSILON);
    }
}
