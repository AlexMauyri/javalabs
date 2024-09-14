package ru.ssau.tk.DoubleA.javalabs.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.lang.Math;

public class nRootCalculateFunctionTest {

    final private static double epsilon = 0.0000001;

    @Test
    void test() {
        nRootCalculateFunction function = new nRootCalculateFunction(6);
        Assertions.assertTrue(Math.abs(function.apply(64.0) - 2) < epsilon);
        function.setRootDegree(2);
        Assertions.assertTrue(Math.abs(function.apply(2.0) - 1.414213562373) < epsilon);
        function.setRootDegree(423);
        Assertions.assertTrue(Math.abs(function.apply(4872938469.0) - 1.054150402759) < epsilon);
    }
}