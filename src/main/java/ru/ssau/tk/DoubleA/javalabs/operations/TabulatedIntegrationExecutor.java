package ru.ssau.tk.DoubleA.javalabs.operations;

import ru.ssau.tk.DoubleA.javalabs.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.SqrFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.TabulatedFunction;

import java.util.concurrent.ExecutionException;

public class TabulatedIntegrationExecutor {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        TabulatedFunction tabulatedFunction = new LinkedListTabulatedFunction(new SqrFunction(), 1, 127, 1500);
        TabulatedIntegrationOperator integrationOperator = new TabulatedIntegrationOperator(100);

        System.out.println(integrationOperator.integrate(tabulatedFunction, 1000));
    }
}
