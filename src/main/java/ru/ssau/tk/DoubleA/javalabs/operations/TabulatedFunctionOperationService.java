package ru.ssau.tk.DoubleA.javalabs.operations;

import ru.ssau.tk.DoubleA.javalabs.exceptions.InconsistentFunctionsException;
import ru.ssau.tk.DoubleA.javalabs.functions.Point;
import ru.ssau.tk.DoubleA.javalabs.functions.TabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.DoubleA.javalabs.functions.factory.TabulatedFunctionFactory;

public class TabulatedFunctionOperationService {
    TabulatedFunctionFactory factory;

    private interface BiOperation {
        double apply(double u, double v);
    }

    public TabulatedFunctionOperationService(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    public TabulatedFunctionOperationService() {
        this.factory = new ArrayTabulatedFunctionFactory();
    }

    private TabulatedFunction doOperation(TabulatedFunction a, TabulatedFunction b, BiOperation operation) {
        if (a.getCount() != b.getCount()) {
            throw new InconsistentFunctionsException();
        }
        int size = a.getCount();

        Point[] points1 = TabulatedFunctionOperationService.asPoints(a);
        Point[] points2 = TabulatedFunctionOperationService.asPoints(b);

        double[] xValues = new double[size];
        double[] yValues = new double[size];

        for (int i = 0; i < size; ++i) {
            if (points1[i].x != points2[i].x) throw new InconsistentFunctionsException();

            xValues[i] = points1[i].x;
            yValues[i] = operation.apply(points1[i].y, points2[i].y);
        }

        return factory.create(xValues, yValues);
    }

    public TabulatedFunction addition(TabulatedFunction a, TabulatedFunction b) {
        return doOperation(a, b, (double x, double y) -> x + y);
    }

    public TabulatedFunction subtraction(TabulatedFunction a, TabulatedFunction b) {
        return doOperation(a, b, (double x, double y) -> x - y);
    }

    public static Point[] asPoints(TabulatedFunction tabulatedFunction) {
        int i = 0;
        Point[] points = new Point[tabulatedFunction.getCount()];
        for (Point point : tabulatedFunction) {
            points[i] = point;
            ++i;
        }

        return points;
    }

    public TabulatedFunctionFactory getFactory() {
        return factory;
    }

    public void setFactory(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }
}
