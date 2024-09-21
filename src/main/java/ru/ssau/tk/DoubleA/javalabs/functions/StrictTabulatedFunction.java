package ru.ssau.tk.DoubleA.javalabs.functions;

import java.util.Iterator;

public class StrictTabulatedFunction {
    TabulatedFunction function;

    public StrictTabulatedFunction(TabulatedFunction function) {
        this.function = function;
    }

    public Iterator<Point> iterator() {
        return function.iterator();
    }

    int getCount() {
        return function.getCount();
    }

    double getX(int index) {
        return function.getX(index);
    }

    double getY(int index) {
        return function.getY(index);
    }

    void setY(int index, double value) {
        function.setY(index, value);
    }

    int indexOfX(double x) {
        return function.indexOfX(x);
    }

    int indexOfY(double y) {
        return function.indexOfY(y);
    }

    double leftBound() {
        return function.leftBound();
    }

    double rightBound() {
        return function.rightBound();
    }

    double apply(double x) throws UnsupportedOperationException {
        int indexX = indexOfX(x);

        if (indexX == -1) {
            throw new UnsupportedOperationException("Not allowed to interpolate y for passed x");
        } else {
            return getY(indexX);
        }
    }
}
