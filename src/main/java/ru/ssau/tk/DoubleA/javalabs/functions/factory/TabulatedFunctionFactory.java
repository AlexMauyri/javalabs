package ru.ssau.tk.DoubleA.javalabs.functions.factory;

import ru.ssau.tk.DoubleA.javalabs.functions.MathFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.StrictTabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.TabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.UnmodifiableTabulatedFunction;

public interface TabulatedFunctionFactory {
    TabulatedFunction create(double[] xValues, double[] yValues);
    TabulatedFunction create(MathFunction source, double xFrom, double xTo, int count);

    default TabulatedFunction createUnmodifiable(double[] xValues, double[] yValues) {
        return new UnmodifiableTabulatedFunction(create(xValues, yValues));
    }

    default TabulatedFunction createStrict(double[] xValues, double[] yValues) {
        return new StrictTabulatedFunction(create(xValues, yValues));
    }

    default TabulatedFunction createStrictUnmodifiable(double[] xValues, double[] yValues) {
        return new StrictTabulatedFunction(
                new UnmodifiableTabulatedFunction(
                        create(xValues, yValues)
                )
        );
    }

}
