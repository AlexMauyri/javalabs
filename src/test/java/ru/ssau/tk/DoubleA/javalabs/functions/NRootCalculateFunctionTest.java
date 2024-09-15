package ru.ssau.tk.DoubleA.javalabs.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NRootCalculateFunctionTest extends AbstractTest {

    @Test
    void test() {
        NRootCalculateFunction function = new NRootCalculateFunction(6);
        Assertions.assertTrue(Math.abs(function.apply(64.0) - 2) <= EPSILON);
        function.setRootDegree(2);
        Assertions.assertTrue(Math.abs(function.apply(2.0) - 1.414213562373) <= EPSILON);
        function.setRootDegree(423);
        Assertions.assertTrue(Math.abs(function.apply(4872938469.0) - 1.054150402759) <= EPSILON);
    }
}