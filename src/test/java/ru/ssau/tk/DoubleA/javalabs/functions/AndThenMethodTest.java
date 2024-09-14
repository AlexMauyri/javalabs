package ru.ssau.tk.DoubleA.javalabs.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AndThenMethodTest {

    final private static double epsilon = 0.0000001;

    @Test
    void test() {

        MathFunction mathFunction = new nRootCalculateFunction(5).andThen(new SqrFunction().andThen(new nRootCalculateFunction(3)));
        Assertions.assertTrue(Math.abs(mathFunction.apply(666) - 2.37937684302674198641370733) <= epsilon);
        Assertions.assertTrue(Math.abs(mathFunction.apply(6345.45345) - 3.213623701) <= epsilon);
        mathFunction = new SqrFunction().andThen(new ConstantFunction(243.24325).andThen(new nRootCalculateFunction(5)));
        Assertions.assertTrue(Math.abs(mathFunction.apply(63342.5) - 3.00060038) <= epsilon);

    }
}
