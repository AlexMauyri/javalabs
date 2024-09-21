package ru.ssau.tk.DoubleA.javalabs.functions;

import java.util.Iterator;

public class StrictTabulatedFunction implements TabulatedFunction {
    TabulatedFunction function;

    public StrictTabulatedFunction(TabulatedFunction function) {
        this.function = function;
    }

    @Override
    public Iterator<Point> iterator() {
        return function.iterator();
    }

    @Override
    public int getCount() {
        return function.getCount();
    }

    @Override
    public double getX(int index) {
        return function.getX(index);
    }

    @Override
    public double getY(int index) {
        return function.getY(index);
    }

    @Override
    public void setY(int index, double value) {
        function.setY(index, value);
    }

    @Override
    public int indexOfX(double x) {
        return function.indexOfX(x);
    }

    @Override
    public int indexOfY(double y) {
        return function.indexOfY(y);
    }

    @Override
    public double leftBound() {
        return function.leftBound();
    }

    @Override
    public double rightBound() {
        return function.rightBound();
    }

    @Override
    public double apply(double x) throws UnsupportedOperationException {
        int indexX = indexOfX(x);

        if (indexX == -1) {
            throw new UnsupportedOperationException("Not allowed to interpolate y for passed x");
        } else {
            return getY(indexX);
        }
    }
}
