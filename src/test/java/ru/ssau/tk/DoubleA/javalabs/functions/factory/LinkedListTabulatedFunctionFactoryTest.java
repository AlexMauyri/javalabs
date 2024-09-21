package ru.ssau.tk.DoubleA.javalabs.functions.factory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.ssau.tk.DoubleA.javalabs.functions.LinkedListTabulatedFunction;

public class LinkedListTabulatedFunctionFactoryTest {
    LinkedListTabulatedFunctionFactory listTabFunFactory = new LinkedListTabulatedFunctionFactory();

    @Test
    void testInstanceOf() {
        Assertions.assertInstanceOf(LinkedListTabulatedFunction.class, listTabFunFactory.create(new double[]{1, 2, 3}, new double[]{1, 4, 9}));
        Assertions.assertInstanceOf(LinkedListTabulatedFunction.class, listTabFunFactory.create(new double[]{1, 3, 9}, new double[]{-1, -9, -81}));
        Assertions.assertInstanceOf(LinkedListTabulatedFunction.class, listTabFunFactory.create(new double[]{-10, -5}, new double[]{100, 25}));
    }
}
