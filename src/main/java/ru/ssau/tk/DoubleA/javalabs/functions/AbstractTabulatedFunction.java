package ru.ssau.tk.DoubleA.javalabs.functions;

abstract public class AbstractTabulatedFunction implements TabulatedFunction
{
    // Count of element pairs in array/list
    protected int count;

    // Finds index of the largest element in array/list, which is less than x.
    abstract protected int floorIndexOfX(double x);

    abstract protected double extrapolateLeft(double x);
    abstract protected double extrapolateRight(double x);
    abstract protected double interpolate(double x, int floorIndex);

    protected double interpolate(double x, double leftX, double rightX, double leftY, double rightY)
    {
        return leftY + (rightY - leftY)/(rightX - leftX) * (x - leftX);
    }

    public double apply(double x)
    {
        if (x < leftBound()) {
            return extrapolateLeft(x);
        }
        else if (x > rightBound()) {
            return extrapolateRight(x);
        }
        else {
            // If x already exists return y, else interpolate value of y
            if (indexOfX(x) != -1) {
                return getY(indexOfX(x));
            }
            else {
                return interpolate(x, floorIndexOfX(x));
            }
        }
    }
}
