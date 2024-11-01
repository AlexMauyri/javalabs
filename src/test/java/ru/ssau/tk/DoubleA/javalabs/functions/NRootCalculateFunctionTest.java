package ru.ssau.tk.DoubleA.javalabs.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NRootCalculateFunctionTest extends AbstractTest {
    NRootCalculateFunction rootFunction = new NRootCalculateFunction(1);

    @Test
    void test() {
        Assertions.assertEquals(741.45, rootFunction.apply(741.45), EPSILON);

        rootFunction.setRootDegree(6);
        Assertions.assertEquals(2, rootFunction.apply(64.0), EPSILON);
        Assertions.assertEquals(6, rootFunction.getRootDegree());

        rootFunction.setRootDegree(2);
        Assertions.assertEquals(1.414213562373, rootFunction.apply(2.0), EPSILON);
        Assertions.assertEquals(2, rootFunction.getRootDegree());

        rootFunction.setRootDegree(423);
        Assertions.assertEquals(1.054150402759, rootFunction.apply(4872938469.0), EPSILON);
        Assertions.assertEquals(423, rootFunction.getRootDegree());
    }
}