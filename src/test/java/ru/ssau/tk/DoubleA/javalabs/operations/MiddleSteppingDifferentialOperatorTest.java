package ru.ssau.tk.DoubleA.javalabs.operations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.ssau.tk.DoubleA.javalabs.functions.AbstractTest;
import ru.ssau.tk.DoubleA.javalabs.functions.MathFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.NRootCalculateFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.SqrFunction;

public class MiddleSteppingDifferentialOperatorTest extends AbstractTest {
    @Test
    void test() {
        double step = 1E-5;
        MiddleSteppingDifferentialOperator function = new MiddleSteppingDifferentialOperator(step);
        MathFunction function1 = function.derive(new SqrFunction());
        Assertions.assertEquals(4.0, function1.apply(2.0), EPSILON);
        Assertions.assertEquals(-4.0, function1.apply(-2.0), EPSILON);
        Assertions.assertEquals(0.0, function1.apply(0.0), EPSILON);
        Assertions.assertEquals(20.0, function1.apply(10.0), EPSILON);
        Assertions.assertEquals(264.6906, function1.apply(132.3453), EPSILON);
        function1 = function.derive(new NRootCalculateFunction(5));
        Assertions.assertEquals(0.114869, function1.apply(2.0), EPSILON);
        Assertions.assertEquals(0.015229, function1.apply(25.0), EPSILON);
        Assertions.assertEquals(0.004248, function1.apply(123.3242), EPSILON);
        Assertions.assertEquals(0.000050, function1.apply(31221.213), EPSILON);
        Assertions.assertEquals(0.000313, function1.apply(3212.3212), EPSILON);
    }
}
