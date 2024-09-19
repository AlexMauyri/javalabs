package ru.ssau.tk.DoubleA.javalabs.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AndThenMethodTest extends AbstractTest {
    MathFunction mathFunction;

    @Test
    void test() {
        mathFunction = new NRootCalculateFunction(5)
                .andThen(new SqrFunction()
                        .andThen(new NRootCalculateFunction(3))
                );
        Assertions.assertEquals(2.379376843, mathFunction.apply(666), EPSILON);
        Assertions.assertEquals(3.213623701, mathFunction.apply(6345.45345), EPSILON);

        mathFunction = new SqrFunction()
                .andThen(new ConstantFunction(243.24325)
                        .andThen(new NRootCalculateFunction(5))
                );
        Assertions.assertEquals(3.00060038, mathFunction.apply(63342.5), EPSILON);

        mathFunction = new NRootCalculateFunction(39)
                .andThen(new SqrFunction()
                        .andThen(new DerivativeFunction(new NRootCalculateFunction(5)))
                );
        Assertions.assertEquals(0.14886014, mathFunction.apply(1337), EPSILON);
    }
}