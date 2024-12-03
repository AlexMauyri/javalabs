package ru.ssau.tk.DoubleA.javalabs.functions;

import ru.ssau.tk.DoubleA.javalabs.exceptions.ArrayIsNotSortedException;
import ru.ssau.tk.DoubleA.javalabs.exceptions.DifferentLengthOfArraysException;

import java.io.Serial;
import java.io.Serializable;
import java.util.Formatter;
import java.math.RoundingMode;
import java.math.BigDecimal;

abstract public class AbstractTabulatedFunction implements TabulatedFunction, Serializable {
    @Serial
    private static final long serialVersionUID = -3369307346104661539L;
    // Count of element pairs in array/list
    protected int count;

    static void checkLengthIsTheSame(double[] xValues, double[] yValues) throws DifferentLengthOfArraysException {
        if (xValues.length != yValues.length) throw new DifferentLengthOfArraysException("Длина массивов должна быть одинакова!");
    }

    static void checkSorted(double[] xValues) throws ArrayIsNotSortedException {
        for (int i = 1; i < xValues.length; ++i) {
            if (xValues[i - 1] >= xValues[i]) throw new ArrayIsNotSortedException("Значения x должны идти в порядке строгого возрастания!");
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        Formatter formatter = new Formatter();
        stringBuilder.append(this.getClass().getSimpleName()).append(" size = ").append(this.count);
        for (Point point : this) {
            stringBuilder.append("\n[").append(BigDecimal.valueOf(point.x)
                    .setScale(6, RoundingMode.HALF_UP)
                    .doubleValue()).append("; ").append(BigDecimal.valueOf(point.y)
                    .setScale(6, RoundingMode.HALF_UP)
                    .doubleValue()).append(']');
        }

        return stringBuilder.toString();
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
