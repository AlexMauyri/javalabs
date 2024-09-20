package ru.ssau.tk.DoubleA.javalabs.functions;

import ru.ssau.tk.DoubleA.javalabs.exceptions.InterpolationException;

import java.util.Arrays;

public class ArrayTabulatedFunction extends AbstractTabulatedFunction implements Insertable, Removable {
    final private static int EXTENSION_MODIFIER = 5;
    final private static double EPSILON = 1E-7;
    private double[] xValues;
    private double[] yValues;

    public ArrayTabulatedFunction(double[] xValues, double[] yValues) throws IllegalArgumentException {
        if (xValues.length < 2 || yValues.length < 2) {
            throw new IllegalArgumentException("Count of Array Tabulated Function nodes cannot be less than 2");
        }

        AbstractTabulatedFunction.checkLengthIsTheSame(xValues, yValues);
        AbstractTabulatedFunction.checkSorted(xValues);

        this.xValues = Arrays.copyOf(xValues, xValues.length);
        this.yValues = Arrays.copyOf(yValues, yValues.length);
        this.count = xValues.length;
    }

    public ArrayTabulatedFunction(MathFunction source, double xFrom, double xTo, int count) throws IllegalArgumentException {
        if (count < 2) {
            throw new IllegalArgumentException("Count of Array Tabulated Function nodes cannot be less than 2");
        }

        if (xFrom > xTo) {
            double temp = xFrom;
            xFrom = xTo;
            xTo = temp;
        }

        xValues = new double[count];
        yValues = new double[count];
        this.count = count;

        if (Math.abs(xFrom - xTo) <= EPSILON) {
            double yValue = source.apply(xFrom);
            for (int i = 0; i < count; ++i) {
                xValues[i] = xFrom;
                yValues[i] = yValue;
            }
        } else {
            xValues[0] = xFrom;
            xValues[count - 1] = xTo;
            yValues[0] = source.apply(xValues[0]);
            yValues[count - 1] = source.apply(xValues[count - 1]);

            double step = (xTo - xFrom) / (count - 1);
            for (int i = 1; i < count - 1; ++i) {
                xValues[i] = xValues[i - 1] + step;
                yValues[i] = source.apply(xValues[i]);
            }
        }
    }

    public void insert(double x, double y) {
        int indexX = indexOfX(x);

        // If x exists change y value, else insert new pair
        if (indexX != -1) {
            yValues[indexX] = y;
        } else {
            double[] xValuesBufferArray, yValuesBufferArray;
            if (count == xValues.length) {
                xValuesBufferArray = new double[count + EXTENSION_MODIFIER];
                yValuesBufferArray = new double[count + EXTENSION_MODIFIER];
            } else {
                xValuesBufferArray = xValues;
                yValuesBufferArray = yValues;
            }

            if (x < leftBound() || x > rightBound()) {
                int startPositionForDestination = (x < leftBound() ? 1 : 0);
                int floorIndexX = (x < leftBound() ? 0 : count);

                System.arraycopy(xValues, 0, xValuesBufferArray, startPositionForDestination, count);
                System.arraycopy(yValues, 0, yValuesBufferArray, startPositionForDestination, count);

                xValuesBufferArray[floorIndexX] = x;
                yValuesBufferArray[floorIndexX] = y;
            } else {
                int floorIndexX = floorIndexOfX(x);
                int numbersBefore = floorIndexX + 1, numbersAfter = count - numbersBefore, newIndex = floorIndexX + 1;

                System.arraycopy(xValues, 0, xValuesBufferArray, 0, numbersBefore);
                System.arraycopy(xValues, newIndex, xValuesBufferArray, newIndex + 1, numbersAfter);
                System.arraycopy(yValues, 0, yValuesBufferArray, 0, numbersBefore);
                System.arraycopy(yValues, newIndex, yValuesBufferArray, newIndex + 1, numbersAfter);

                xValuesBufferArray[newIndex] = x;
                yValuesBufferArray[newIndex] = y;
            }

            xValues = xValuesBufferArray;
            yValues = yValuesBufferArray;
            count++;
        }
    }

    public void remove(int index) throws ArrayIndexOutOfBoundsException, IllegalStateException {
        if (index < 0) {
            throw new ArrayIndexOutOfBoundsException("Index cannot be less than zero");
        } else if (index >= count) {
            throw new ArrayIndexOutOfBoundsException("Index cannot be greater than or equal to count");
        } else if (count == 2) {
            throw new IllegalStateException("Count of List Tabulated Function nodes cannot be less than 2");
        }

        for (int i = index; i < count - 1; ++i) {
            xValues[i] = xValues[i + 1];
            yValues[i] = yValues[i + 1];
        }

        if (count - 1 <= xValues.length - EXTENSION_MODIFIER) {
            double[] newXValues = new double[count - 1];
            double[] newYValues = new double[count - 1];

            System.arraycopy(xValues, 0, newXValues, 0, count - 1);
            System.arraycopy(yValues, 0, newYValues, 0, count - 1);

            xValues = newXValues;
            yValues = newYValues;
        }

        --count;
    }

    @Override
    protected int floorIndexOfX(double x) throws IllegalArgumentException {
        if (x < leftBound()) {
            throw new IllegalArgumentException("Index cannot be less than left bound");
        }

        int index = 0;

        for (int i = 0; xValues[i] < x && i < count; ++i) {
            index = i;
        }

        return (index == count - 1 && xValues[index] < x ? count : index);
    }

    @Override
    protected double extrapolateLeft(double x) {
        if (xValues[0] == xValues[count - 1]) {
            return yValues[0];
        }

        return interpolate(x, getX(0), getX(1), getY(0), getY(1));
    }

    @Override
    protected double extrapolateRight(double x) {
        if (xValues[0] == xValues[count - 1]) {
            return yValues[0];
        }

        return interpolate(x, getX(count - 2), getX(count - 1), getY(count - 2), getY(count - 1));
    }

    @Override
    protected double interpolate(double x, int floorIndex) throws IllegalArgumentException {
        if (floorIndex < 0) {
            throw new IllegalArgumentException("Floor Index cannot be less than zero");
        } else if (floorIndex >= count - 1) {
            throw new IllegalArgumentException("Floor Index cannot be greater than or equal to last node index");
        }

        if (xValues[0] == xValues[count - 1]) {
            return yValues[0];
        }

        if (!(x > getX(floorIndex) && x < getX(floorIndex + 1))) {
            throw new InterpolationException();
        }

        return interpolate(x, getX(floorIndex), getX(floorIndex + 1), getY(floorIndex), getY(floorIndex + 1));
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public double getX(int index) throws ArrayIndexOutOfBoundsException {
        if (index < 0) {
            throw new ArrayIndexOutOfBoundsException("Index cannot be less than zero");
        } else if (index >= count) {
            throw new ArrayIndexOutOfBoundsException("Index cannot be greater than or equal to count");
        }

        return xValues[index];
    }

    @Override
    public double getY(int index) throws ArrayIndexOutOfBoundsException {
        if (index < 0) {
            throw new ArrayIndexOutOfBoundsException("Index cannot be less than zero");
        } else if (index >= count) {
            throw new ArrayIndexOutOfBoundsException("Index cannot be greater than or equal to count");
        }

        return yValues[index];
    }

    @Override
    public void setY(int index, double value) throws ArrayIndexOutOfBoundsException {
        if (index < 0) {
            throw new ArrayIndexOutOfBoundsException("Index cannot be less than zero");
        } else if (index >= count) {
            throw new ArrayIndexOutOfBoundsException("Index cannot be greater than or equal to count");
        }

        yValues[index] = value;
    }

    @Override
    public int indexOfX(double x) {
        for (int i = 0; i < count; ++i) {
            if (Math.abs(xValues[i] - x) < EPSILON) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int indexOfY(double y) {
        for (int i = 0; i < count; ++i) {
            if (Math.abs(yValues[i] - y) < EPSILON) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public double leftBound() {
        return xValues[0];
    }

    @Override
    public double rightBound() {
        return xValues[count - 1];
    }
}
