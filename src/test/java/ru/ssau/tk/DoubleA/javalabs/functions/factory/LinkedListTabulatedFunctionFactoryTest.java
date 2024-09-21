package ru.ssau.tk.DoubleA.javalabs.functions.factory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.ssau.tk.DoubleA.javalabs.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.StrictTabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.TabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.UnmodifiableTabulatedFunction;

public class LinkedListTabulatedFunctionFactoryTest {
    LinkedListTabulatedFunctionFactory listTabFunFactory = new LinkedListTabulatedFunctionFactory();

    @Test
    void testInstanceOf() {
        Assertions.assertInstanceOf(LinkedListTabulatedFunction.class, listTabFunFactory.create(new double[]{1, 2, 3}, new double[]{1, 4, 9}));
        Assertions.assertInstanceOf(LinkedListTabulatedFunction.class, listTabFunFactory.create(new double[]{1, 3, 9}, new double[]{-1, -9, -81}));
        Assertions.assertInstanceOf(LinkedListTabulatedFunction.class, listTabFunFactory.create(new double[]{-10, -5}, new double[]{100, 25}));
    }

    TabulatedFunction tabulatedFunction;

    @Test
    void testUnmodifiable() {
        tabulatedFunction = listTabFunFactory.createUnmodifiable(new double[]{1, 3, 9}, new double[]{-1, -9, -81});
        Assertions.assertInstanceOf(UnmodifiableTabulatedFunction.class, tabulatedFunction);

        Assertions.assertThrows(UnsupportedOperationException.class, () -> tabulatedFunction.setY(0, 1));
        Assertions.assertThrows(UnsupportedOperationException.class, () -> tabulatedFunction.setY(3, 1));

        tabulatedFunction.apply(3);
        tabulatedFunction.apply(2);
    }

    @Test
    void testStrict() {
        tabulatedFunction = listTabFunFactory.createStrict(new double[]{1, 3, 9}, new double[]{-1, -9, -81});
        Assertions.assertInstanceOf(StrictTabulatedFunction.class, tabulatedFunction);

        tabulatedFunction.setY(0, 1);
        tabulatedFunction.setY(2, 81);

        tabulatedFunction.apply(3);
        Assertions.assertThrows(UnsupportedOperationException.class, () -> tabulatedFunction.apply(10));
    }

    @Test
    void testUnmodifiableStrict() {
        tabulatedFunction = listTabFunFactory.createStrictUnmodifiable(new double[]{1, 3, 9}, new double[]{-1, -9, -81});

        Assertions.assertThrows(UnsupportedOperationException.class, () -> tabulatedFunction.setY(0, 1));
        Assertions.assertThrows(UnsupportedOperationException.class, () -> tabulatedFunction.setY(3, 1));

        tabulatedFunction.apply(3);
        Assertions.assertThrows(UnsupportedOperationException.class, () -> tabulatedFunction.apply(7));
    }
}
