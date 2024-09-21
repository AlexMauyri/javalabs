package ru.ssau.tk.DoubleA.javalabs.operations;

import ru.ssau.tk.DoubleA.javalabs.functions.Point;
import ru.ssau.tk.DoubleA.javalabs.functions.TabulatedFunction;

public class TabulatedFunctionOperationService {
    public static Point[] asPoints(TabulatedFunction tabulatedFunction) {
        int i = 0;
        Point[] points = new Point[tabulatedFunction.getCount()];
        for (Point point : tabulatedFunction) {
            points[i] = point;
            ++i;
        }

        return points;
    }
}
