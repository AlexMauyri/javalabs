package ru.ssau.tk.DoubleA.javalabs.operations;

import com.fasterxml.jackson.annotation.JsonCreator;
import ru.ssau.tk.DoubleA.javalabs.exceptions.InconsistentFunctionsException;
import ru.ssau.tk.DoubleA.javalabs.functions.Point;
import ru.ssau.tk.DoubleA.javalabs.functions.TabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.DoubleA.javalabs.functions.factory.TabulatedFunctionFactory;

public class TabulatedFunctionOperationService {
    TabulatedFunctionFactory factory;

    public TabulatedFunctionOperationService(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    @JsonCreator
    public TabulatedFunctionOperationService() {
        this.factory = new ArrayTabulatedFunctionFactory();
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

    private TabulatedFunction doOperation(TabulatedFunction a, TabulatedFunction b, BiOperation operation) throws InconsistentFunctionsException {
        if (a.getCount() != b.getCount()) {
            throw new InconsistentFunctionsException("Число точек различно в двух функциях!");
        }
        int size = a.getCount();

        Point[] points1 = TabulatedFunctionOperationService.asPoints(a);
        Point[] points2 = TabulatedFunctionOperationService.asPoints(b);

        double[] xValues = new double[size];
        double[] yValues = new double[size];

        for (int i = 0; i < size; ++i) {
            if (points1[i].x != points2[i].x) throw new InconsistentFunctionsException("Неодинаковые значения x на позиции %d".formatted(i + 1));

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

    public TabulatedFunction multiply(TabulatedFunction a, TabulatedFunction b) {
        return doOperation(a, b, (double x, double y) -> x * y);
    }

    public TabulatedFunction divide(TabulatedFunction a, TabulatedFunction b) {
        return doOperation(a, b, (double x, double y) -> {
            if (y == 0) throw new IllegalArgumentException("Делить на ноль нельзя!");
            return x / y;
        });
    }

    public TabulatedFunctionFactory getFactory() {
        return factory;
    }

    public void setFactory(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    private interface BiOperation {
        double apply(double u, double v);
    }
}
