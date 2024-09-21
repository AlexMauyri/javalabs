package ru.ssau.tk.DoubleA.javalabs.functions.factory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.ssau.tk.DoubleA.javalabs.functions.ArrayTabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.StrictTabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.TabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.UnmodifiableTabulatedFunction;

public class ArrayTabulatedFunctionFactoryTest {
    ArrayTabulatedFunctionFactory arrayTabFunFactory = new ArrayTabulatedFunctionFactory();

    @Test
    void testInstanceOf() {
        Assertions.assertInstanceOf(ArrayTabulatedFunction.class, arrayTabFunFactory.create(new double[]{1, 2, 3}, new double[]{1, 4, 9}));
        Assertions.assertInstanceOf(ArrayTabulatedFunction.class, arrayTabFunFactory.create(new double[]{1, 3, 9}, new double[]{-1, -9, -81}));
        Assertions.assertInstanceOf(ArrayTabulatedFunction.class, arrayTabFunFactory.create(new double[]{-10, -5}, new double[]{100, 25}));
    }

    TabulatedFunction tabulatedFunction;

    @Test
    void testUnmodifiable() {
        tabulatedFunction = arrayTabFunFactory.createUnmodifiable(new double[]{1, 3, 9}, new double[]{-1, -9, -81});
        Assertions.assertInstanceOf(UnmodifiableTabulatedFunction.class, tabulatedFunction);

        Assertions.assertThrows(UnsupportedOperationException.class, () -> tabulatedFunction.setY(0, 1));
        Assertions.assertThrows(UnsupportedOperationException.class, () -> tabulatedFunction.setY(3, 1));

        tabulatedFunction.apply(3);
        tabulatedFunction.apply(2);
    }

    @Test
    void testStrict() {
        tabulatedFunction = arrayTabFunFactory.createStrict(new double[]{1, 3, 9}, new double[]{-1, -9, -81});
        Assertions.assertInstanceOf(StrictTabulatedFunction.class, tabulatedFunction);

        tabulatedFunction.setY(0, 1);
        tabulatedFunction.setY(2, 81);

        tabulatedFunction.apply(3);
        Assertions.assertThrows(UnsupportedOperationException.class, () -> tabulatedFunction.apply(10));
    }

    @Test
    void testUnmodifiableStrict() {
        tabulatedFunction = arrayTabFunFactory.createStrictUnmodifiable(new double[]{1, 3, 9}, new double[]{-1, -9, -81});

        Assertions.assertThrows(UnsupportedOperationException.class, () -> tabulatedFunction.setY(0, 1));
        Assertions.assertThrows(UnsupportedOperationException.class, () -> tabulatedFunction.setY(3, 1));

        tabulatedFunction.apply(3);
        Assertions.assertThrows(UnsupportedOperationException.class, () -> tabulatedFunction.apply(7));
    }
}
