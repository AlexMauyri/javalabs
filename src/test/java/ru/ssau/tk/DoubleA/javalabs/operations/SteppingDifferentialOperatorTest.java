package ru.ssau.tk.DoubleA.javalabs.operations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.ssau.tk.DoubleA.javalabs.functions.AbstractTest;

public class SteppingDifferentialOperatorTest extends AbstractTest {

    @Test
    void test() {
        SteppingDifferentialOperator operator = new RightSteppingDifferentialOperator(1E-5);
        Assertions.assertEquals(1E-5, operator.getStep(), EPSILON);
        operator.setStep(1E-10);
        Assertions.assertEquals(1E-10, operator.getStep(), EPSILON);
        Assertions.assertThrows(IllegalArgumentException.class, () -> operator.setStep(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> operator.setStep(Double.POSITIVE_INFINITY));
        Assertions.assertThrows(IllegalArgumentException.class, () -> operator.setStep(Double.NaN));
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new RightSteppingDifferentialOperator(-1);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new RightSteppingDifferentialOperator(Double.POSITIVE_INFINITY);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new RightSteppingDifferentialOperator(Double.NaN);
        });
    }
}
