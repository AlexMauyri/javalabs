package ru.ssau.tk.DoubleA.javalabs.operations;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.ssau.tk.DoubleA.javalabs.functions.*;

import java.util.concurrent.ExecutionException;

public class TabulatedIntegrationOperatorTest extends AbstractTest {

    int maxAllowedThreads = Runtime.getRuntime().availableProcessors()-1;

    @Test
    void integrateTest() throws ExecutionException, InterruptedException {
        TabulatedIntegrationOperator integrationOperator = new TabulatedIntegrationOperator(maxAllowedThreads);

        TabulatedFunction tabulatedFunction = new LinkedListTabulatedFunction(new SqrFunction(), 1, 127, 12700);
        Assertions.assertEquals(682794, integrationOperator.integrate(tabulatedFunction, 100), EPSILON);

        tabulatedFunction = new LinkedListTabulatedFunction(new SqrFunction(), 3, 300, 10000);
        Assertions.assertEquals(8_999_991, integrationOperator.integrate(tabulatedFunction, 100), EPSILON);

        tabulatedFunction = new LinkedListTabulatedFunction(new NRootCalculateFunction(5), 1, 100, 10000);
        Assertions.assertEquals(208.49, integrationOperator.integrate(tabulatedFunction, 100), EPSILON);

        tabulatedFunction = new LinkedListTabulatedFunction(new DerivativeFunction(new SqrFunction()), -20, 50, 10000);
        Assertions.assertEquals(2100, integrationOperator.integrate(tabulatedFunction, 100), EPSILON);

        integrationOperator.shutdown();

        integrationOperator = new TabulatedIntegrationOperator();

        tabulatedFunction = new LinkedListTabulatedFunction(new SqrFunction(), 1, 127, 12700);
        Assertions.assertEquals(682794, integrationOperator.integrate(tabulatedFunction, 100), EPSILON);

        tabulatedFunction = new LinkedListTabulatedFunction(new SqrFunction(), 3, 300, 10000);
        Assertions.assertEquals(8_999_991, integrationOperator.integrate(tabulatedFunction, 100), EPSILON);

        TabulatedIntegrationOperator finalIntegrationOperator = integrationOperator;
        Assertions.assertThrows(UnsupportedOperationException.class, () -> finalIntegrationOperator.apply(5));

        integrationOperator.shutdown();
    }

}
