package ru.ssau.tk.DoubleA.javalabs.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ssau.tk.DoubleA.javalabs.exceptions.ArrayIsNotSortedException;
import ru.ssau.tk.DoubleA.javalabs.exceptions.DifferentLengthOfArraysException;

public class AbstractTabulatedFunctionTest extends AbstractTest {

    double[] xValues;
    double[] yValues;

    @BeforeEach
    void init() {
        xValues = new double[]{1.0, 2.0, 3.5, 4.5};
        yValues = new double[]{4.4, 2.5, 3.2, 4.1};
    }

    @Test
    void checkLengthIsTheSameTest() {
        Assertions.assertDoesNotThrow(() -> AbstractTabulatedFunction.checkLengthIsTheSame(xValues, yValues));
        xValues = new double[]{1.4, 2.4, 3.5, 3.6, 4.5};
        Assertions.assertThrows(DifferentLengthOfArraysException.class, () -> AbstractTabulatedFunction.checkLengthIsTheSame(xValues, yValues));
        xValues = new double[]{1.4, 2.5};
        Assertions.assertThrows(DifferentLengthOfArraysException.class, () -> AbstractTabulatedFunction.checkLengthIsTheSame(xValues, yValues));
        yValues = new double[]{3.5, 2.5};
        Assertions.assertDoesNotThrow(() -> AbstractTabulatedFunction.checkLengthIsTheSame(xValues, yValues));
        Assertions.assertEquals("Hello", new ArrayIsNotSortedException("Hello").getMessage());
        Assertions.assertEquals("Hello", new DifferentLengthOfArraysException("Hello").getMessage());
    }

    @Test
    void checkSortedTest() {
        Assertions.assertDoesNotThrow(() -> AbstractTabulatedFunction.checkSorted(xValues));
        xValues = new double[]{1.0, 1.0, 2.0, 3.5};
        Assertions.assertThrows(ArrayIsNotSortedException.class, () -> AbstractTabulatedFunction.checkSorted(xValues));
        xValues = new double[]{1.0, 1.2, 3.0, 2.5};
        Assertions.assertThrows(ArrayIsNotSortedException.class, () -> AbstractTabulatedFunction.checkSorted(xValues));
        xValues = new double[]{-1.5, 0.0, 2.5, 3.5};
        Assertions.assertDoesNotThrow(() -> AbstractTabulatedFunction.checkSorted(xValues));
    }
}
