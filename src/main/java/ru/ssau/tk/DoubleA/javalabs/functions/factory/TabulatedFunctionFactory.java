package ru.ssau.tk.DoubleA.javalabs.functions.factory;

import ru.ssau.tk.DoubleA.javalabs.functions.TabulatedFunction;

public interface TabulatedFunctionFactory {
    TabulatedFunction create(double[] xValues, double[] yValues);
}
