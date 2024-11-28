package ru.ssau.tk.DoubleA.javalabs.functions.factory;

import ru.ssau.tk.DoubleA.javalabs.functions.ArrayTabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.MathFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.TabulatedFunction;

public class ArrayTabulatedFunctionFactory implements TabulatedFunctionFactory {
    @Override
    public TabulatedFunction create(double[] xValues, double[] yValues) {
        return new ArrayTabulatedFunction(xValues, yValues);
    }

    @Override
    public TabulatedFunction create(MathFunction source, double xFrom, double xTo, int count) {
        return new ArrayTabulatedFunction(source, xFrom, xTo, count);
    }
}
