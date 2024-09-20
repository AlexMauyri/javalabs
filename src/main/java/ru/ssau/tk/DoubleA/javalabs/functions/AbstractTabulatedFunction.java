package ru.ssau.tk.DoubleA.javalabs.functions;

import ru.ssau.tk.DoubleA.javalabs.exceptions.ArrayIsNotSortedException;
import ru.ssau.tk.DoubleA.javalabs.exceptions.DifferentLengthOfArraysException;

abstract public class AbstractTabulatedFunction implements TabulatedFunction {
    // Count of element pairs in array/list
    protected int count;

    static void checkLengthIsTheSame(double[] xValues, double[] yValues) {
        if (xValues.length != yValues.length) throw new DifferentLengthOfArraysException();
    }

    static void checkSorted(double[] xValues) {
        for (int i = 1; i < xValues.length; ++i) {
            if (xValues[i - 1] >= xValues[i]) throw new ArrayIsNotSortedException();
        }
    }

    // Finds index of the largest element in array/list, which is less than x.
    abstract protected int floorIndexOfX(double x);

    abstract protected double extrapolateLeft(double x);

    abstract protected double extrapolateRight(double x);

    abstract protected double interpolate(double x, int floorIndex);

    protected double interpolate(double x, double leftX, double rightX, double leftY, double rightY) {
        return leftY + (rightY - leftY) / (rightX - leftX) * (x - leftX);
    }

    public double apply(double x) {
        if (x < leftBound()) {
            return extrapolateLeft(x);
        } else if (x > rightBound()) {
            return extrapolateRight(x);
        } else {
            // If x already exists return y, else interpolate value of y
            if (indexOfX(x) != -1) {
                return getY(indexOfX(x));
            } else {
                return interpolate(x, floorIndexOfX(x));
            }
        }
    }
}
