package ru.ssau.tk.DoubleA.javalabs.operations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.ssau.tk.DoubleA.javalabs.concurrent.SynchronizedTabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.ArrayTabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.TabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.DoubleA.javalabs.functions.factory.LinkedListTabulatedFunctionFactory;

public class TabulatedDifferentialOperatorTest {
    TabulatedFunction tabulatedFunction;
    TabulatedDifferentialOperator differentialOperator;

    @Test
    void testArrayTabulatedFunction() {
        tabulatedFunction = new ArrayTabulatedFunction(new double[]{-3, 1.5, 6, 10.5, 15}, new double[]{9, 2.25, 36, 110.25, 225});
        differentialOperator = new TabulatedDifferentialOperator();
        Assertions.assertThrows(UnsupportedOperationException.class, () -> differentialOperator.apply(6));
        tabulatedFunction = differentialOperator.derive(tabulatedFunction);
        Assertions.assertInstanceOf(ArrayTabulatedFunction.class, tabulatedFunction);

        Assertions.assertEquals(16.5, tabulatedFunction.apply(6));
        Assertions.assertEquals(25.5, tabulatedFunction.apply(10.5));
        Assertions.assertEquals(tabulatedFunction.apply(10.5), tabulatedFunction.apply(15));
        Assertions.assertEquals(12.5, tabulatedFunction.apply(4));
        Assertions.assertEquals(-7.5, tabulatedFunction.apply(-6));
        Assertions.assertEquals(25.5, tabulatedFunction.apply(27));

    }

    @Test
    void testListTabulatedFunction() {
        tabulatedFunction = new LinkedListTabulatedFunction(new double[]{-3, 1.5, 6, 10.5, 15}, new double[]{9, 2.25, 36, 110.25, 225});
        LinkedListTabulatedFunctionFactory factory = new LinkedListTabulatedFunctionFactory();
        differentialOperator = new TabulatedDifferentialOperator(factory);
        tabulatedFunction = differentialOperator.derive(tabulatedFunction);
        Assertions.assertEquals(factory, differentialOperator.getFactory());
        Assertions.assertThrows(UnsupportedOperationException.class, () -> differentialOperator.apply(6));
        ArrayTabulatedFunctionFactory factory1 = new ArrayTabulatedFunctionFactory();
        differentialOperator.setFactory(factory1);
        Assertions.assertEquals(factory1, differentialOperator.getFactory());
        Assertions.assertInstanceOf(LinkedListTabulatedFunction.class, tabulatedFunction);

        Assertions.assertEquals(16.5, tabulatedFunction.apply(6));
        Assertions.assertEquals(25.5, tabulatedFunction.apply(10.5));
        Assertions.assertEquals(tabulatedFunction.apply(10.5), tabulatedFunction.apply(15));
        Assertions.assertEquals(22.5, tabulatedFunction.apply(9));
        Assertions.assertEquals(0.5, tabulatedFunction.apply(-2));
        Assertions.assertEquals(25.5, tabulatedFunction.apply(2147259312));

    }

    @Test
    void testSynchronization() {
        tabulatedFunction = new LinkedListTabulatedFunction(new double[]{-3, 1.5, 6, 10.5, 15}, new double[]{9, 2.25, 36, 110.25, 225});
        differentialOperator = new TabulatedDifferentialOperator(new LinkedListTabulatedFunctionFactory());
        tabulatedFunction = differentialOperator.deriveSynchronously(tabulatedFunction);

        Assertions.assertEquals(16.5, tabulatedFunction.apply(6));
        Assertions.assertEquals(25.5, tabulatedFunction.apply(10.5));
        Assertions.assertEquals(tabulatedFunction.apply(10.5), tabulatedFunction.apply(15));
        Assertions.assertEquals(22.5, tabulatedFunction.apply(9));
        Assertions.assertEquals(0.5, tabulatedFunction.apply(-2));
        Assertions.assertEquals(25.5, tabulatedFunction.apply(2147259312));

        tabulatedFunction = new ArrayTabulatedFunction(new double[]{-3, 1.5, 6, 10.5, 15}, new double[]{9, 2.25, 36, 110.25, 225});
        tabulatedFunction = differentialOperator.deriveSynchronously(tabulatedFunction);

        Assertions.assertEquals(16.5, tabulatedFunction.apply(6));
        Assertions.assertEquals(25.5, tabulatedFunction.apply(10.5));
        Assertions.assertEquals(tabulatedFunction.apply(10.5), tabulatedFunction.apply(15));
        Assertions.assertEquals(12.5, tabulatedFunction.apply(4));
        Assertions.assertEquals(-7.5, tabulatedFunction.apply(-6));
        Assertions.assertEquals(25.5, tabulatedFunction.apply(27));

        tabulatedFunction = new SynchronizedTabulatedFunction(
                new ArrayTabulatedFunction(new double[]{-3, 1.5, 6, 10.5, 15}, new double[]{9, 2.25, 36, 110.25, 225})
        );
        tabulatedFunction = differentialOperator.deriveSynchronously(tabulatedFunction);
        Assertions.assertEquals(16.5, tabulatedFunction.apply(6));
        Assertions.assertEquals(25.5, tabulatedFunction.apply(10.5));
        Assertions.assertEquals(tabulatedFunction.apply(10.5), tabulatedFunction.apply(15));
        Assertions.assertEquals(12.5, tabulatedFunction.apply(4));
    }
}
