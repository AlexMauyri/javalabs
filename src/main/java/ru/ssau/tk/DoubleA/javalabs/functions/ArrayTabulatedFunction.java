package ru.ssau.tk.DoubleA.javalabs.functions;

import java.util.Arrays;

public class ArrayTabulatedFunction extends AbstractTabulatedFunction implements Insertable, Removable {
    private double[] xValues;
    private double[] yValues;

    final private static int EXTENSION_CONSTANT = 5;
    protected int emptyPlaces = 0;

    public void insert(double x, double y) {

        int indexX = indexOfX(x);
        if (indexX != -1) {
            yValues[indexX] = y;
        }
        else {
            double[] xValuesBuffer, yValuesBuffer;
            if (emptyPlaces == 0) {
                xValuesBuffer = new double[count + EXTENSION_CONSTANT];
                yValuesBuffer = new double[count + EXTENSION_CONSTANT];
                emptyPlaces = EXTENSION_CONSTANT - 1;
            } else {
                xValuesBuffer = xValues;
                yValuesBuffer = yValues;
                emptyPlaces--;
            }

            if (x < leftBound()) {
                System.arraycopy(xValues, 0, xValuesBuffer, 1, count);
                System.arraycopy(yValues, 0, yValuesBuffer, 1, count);
                xValuesBuffer[0] = x;
                yValuesBuffer[0] = y;
            }
            else if (x > rightBound()) {
                System.arraycopy(xValues, 0, xValuesBuffer, 0, count);
                System.arraycopy(yValues, 0, yValuesBuffer, 0, count);
                xValuesBuffer[count] = x;
                yValuesBuffer[count] = y;
            }
            else {
                int floorIndexX = floorIndexOfX(x);
                int numbersBefore = floorIndexX + 1, numbersAfter = count - numbersBefore, newIndex = floorIndexX + 1;
                System.arraycopy(xValues, 0, xValuesBuffer, 0, numbersBefore);
                System.arraycopy(xValues, newIndex, xValuesBuffer, newIndex + 1, numbersAfter);
                System.arraycopy(yValues, 0, yValuesBuffer, 0, numbersBefore);
                System.arraycopy(yValues, newIndex, yValuesBuffer, newIndex + 1, numbersAfter);
                xValuesBuffer[newIndex] = x;
                yValuesBuffer[newIndex] = y;
            }

            xValues = xValuesBuffer;
            yValues = yValuesBuffer;
            count++;
        }
    }

    public ArrayTabulatedFunction(double[] xValues, double[] yValues) {
        this.xValues = Arrays.copyOf(xValues, xValues.length);
        this.yValues = Arrays.copyOf(yValues, yValues.length);
        this.count = xValues.length;
    }

    public ArrayTabulatedFunction(MathFunction source, double xFrom, double xTo, int count) {
        if (xFrom > xTo) {
            double temp = xFrom;
            xFrom = xTo;
            xTo = temp;
        }

        xValues = new double[count];
        yValues = new double[count];
        this.count = count;

        if (Math.abs(xFrom - xTo) <= 0.0000001) {
            double yValue = source.apply(xFrom);
            for (int i = 0; i < count; ++i) {
                xValues[i] = xFrom;
                yValues[i] = yValue;
            }
        }
        else if (count >= 2) {
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

    public void remove(int index) {
        if (index < 0 || index >= count) return;

        for (int i = index; i < count - 1; ++i) {
            xValues[i] = xValues[i + 1];
            yValues[i] = yValues[i + 1];
        }

        if (count - 1 <= xValues.length / 2) {
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
    protected int floorIndexOfX(double x) {
        int index = 0;
        while (index < count && xValues[index] < x) {
            ++index;
        }
        return index;
    }

    @Override
    protected double extrapolateLeft(double x) throws ArrayIndexOutOfBoundsException {
        if (count == 1) return x;

        return interpolate(x, 0);
    }

    @Override
    protected double extrapolateRight(double x) throws ArrayIndexOutOfBoundsException {
        if (count == 1) return x;

        return interpolate(x, count - 2);
    }

    @Override
    protected double interpolate(double x, int floorIndex) throws ArrayIndexOutOfBoundsException {
        if (count == 1) return x;

        return interpolate(x, getX(floorIndex), getX(floorIndex + 1), getY(floorIndex), getY(floorIndex + 1));
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public double getX(int index) throws ArrayIndexOutOfBoundsException {
        return xValues[index];
    }

    @Override
    public double getY(int index) throws ArrayIndexOutOfBoundsException {
        return yValues[index];
    }

    @Override
    public void setY(int index, double value) throws ArrayIndexOutOfBoundsException {
        yValues[index] = value;
    }

    @Override
    public int indexOfX(double x) {
        for (int i = 0; i < count; ++i) {
            if (Math.abs(xValues[i] - x) < 0.000001) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int indexOfY(double y) {
        for (int i = 0; i < count; ++i) {
            if (Math.abs(yValues[i] - y) < 0.000001) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public double leftBound() throws ArrayIndexOutOfBoundsException {
        return xValues[0];
    }

    @Override
    public double rightBound() throws ArrayIndexOutOfBoundsException {
        return xValues[count - 1];
    }
}
