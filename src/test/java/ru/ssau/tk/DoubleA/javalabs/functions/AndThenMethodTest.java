package ru.ssau.tk.DoubleA.javalabs.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AndThenMethodTest extends AbstractTest {

    @Test
    void test() {

        MathFunction mathFunction = new NRootCalculateFunction(5).andThen(new SqrFunction().andThen(new NRootCalculateFunction(3)));
        Assertions.assertTrue(Math.abs(mathFunction.apply(666) - 2.37937684302674198641370733) <= EPSILON);
        Assertions.assertTrue(Math.abs(mathFunction.apply(6345.45345) - 3.213623701) <= EPSILON);

        mathFunction = new SqrFunction().andThen(new ConstantFunction(243.24325).andThen(new NRootCalculateFunction(5)));
        Assertions.assertTrue(Math.abs(mathFunction.apply(63342.5) - 3.00060038) <= EPSILON);

        mathFunction = new NRootCalculateFunction(39).andThen(new SqrFunction().andThen(new DerivativeFunction(new NRootCalculateFunction(5))));
        Assertions.assertTrue(Math.abs(0.14886014 - mathFunction.apply(1337)) <= EPSILON);
    }
}