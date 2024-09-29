package ru.ssau.tk.DoubleA.javalabs.concurrent;

import ru.ssau.tk.DoubleA.javalabs.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.SqrFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.TabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.operations.TabulatedIntegrationOperator;

import java.util.concurrent.ExecutionException;

public class TabulatedIntegrationExecutor {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        TabulatedIntegrationOperator integrationOperator = new TabulatedIntegrationOperator();

        TabulatedFunction tabulatedFunction = new LinkedListTabulatedFunction(new SqrFunction(), 1, 127, 100000);
        System.out.println(integrationOperator.integrate(tabulatedFunction, 100));

        integrationOperator.shutdown();
    }
}