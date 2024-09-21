package ru.ssau.tk.DoubleA.javalabs.operations;

import ru.ssau.tk.DoubleA.javalabs.functions.Point;
import ru.ssau.tk.DoubleA.javalabs.functions.TabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.DoubleA.javalabs.functions.factory.TabulatedFunctionFactory;

public class TabulatedDifferentialOperator implements DifferentialOperator<TabulatedFunction>{
    private TabulatedFunctionFactory factory;

    public TabulatedDifferentialOperator(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    public TabulatedDifferentialOperator() {
        this.factory = new ArrayTabulatedFunctionFactory();
    }

    public TabulatedFunctionFactory getFactory() {
        return factory;
    }

    public void setFactory(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    @Override
    public TabulatedFunction derive(TabulatedFunction function) {
        Point[] points = TabulatedFunctionOperationService.asPoints(function);

        int pointsLength = points.length;
        double[] xValues = new double[pointsLength];
        double[] yValues = new double[pointsLength];

        int index;
        for (index = 0; index < pointsLength - 1; index++) {
            xValues[index] = points[index].x;
            yValues[index] = (points[index+1].y - points[index].y)/(points[index+1].x-points[index].x);
        }
        xValues[index] = points[index].x;
        yValues[pointsLength - 1] = yValues[index - 1];

        return factory.create(xValues, yValues);
    }

    @Override
    public double apply(double x) {
        throw new UnsupportedOperationException();
    }
}