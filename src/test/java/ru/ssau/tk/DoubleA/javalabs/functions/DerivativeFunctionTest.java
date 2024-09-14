package ru.ssau.tk.DoubleA.javalabs.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DerivativeFunctionTest {

    private final static double epsilon = Math.pow(1, -5);

    @Test
    void test() {
        DerivativeFunction derivativeFunction = new DerivativeFunction(new SqrFunction());
        Assertions.assertTrue(Math.abs(8 - derivativeFunction.apply(4)) <= epsilon);
        derivativeFunction = new DerivativeFunction(new UnitFunction());
        Assertions.assertTrue(Math.abs(0 - derivativeFunction.apply(4)) <= epsilon);
        derivativeFunction = new DerivativeFunction(new nRootCalculateFunction(3).andThen(new SqrFunction()));
        Assertions.assertTrue(Math.abs(0.0879857462720519825939632373 - derivativeFunction.apply(435)) <= epsilon);
    }
}
