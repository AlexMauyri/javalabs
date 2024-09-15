package ru.ssau.tk.DoubleA.javalabs.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DerivativeFunctionTest extends AbstractTest {

    @Test
    void test() {
        DerivativeFunction derivativeFunction = new DerivativeFunction(new SqrFunction());
        Assertions.assertEquals(8, derivativeFunction.apply(4), EPSILON);

        derivativeFunction = new DerivativeFunction(new UnitFunction());
        Assertions.assertEquals(0, derivativeFunction.apply(4), EPSILON);

        derivativeFunction = new DerivativeFunction(new NRootCalculateFunction(3).andThen(new SqrFunction()));
        Assertions.assertEquals(0.087985746, derivativeFunction.apply(435), EPSILON);

        derivativeFunction = new DerivativeFunction(new CompositeFunction(new NRootCalculateFunction(5), new NRootCalculateFunction(6).andThen(new SqrFunction())));
        Assertions.assertEquals(0.00015440, derivativeFunction.apply(666), EPSILON);
    }
}
