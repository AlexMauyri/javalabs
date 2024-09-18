package ru.ssau.tk.DoubleA.javalabs.functions;

public class MockTabulatedFunction extends AbstractTabulatedFunction
{
    private double x0, x1, y0, y1;

    public MockTabulatedFunction(double x0, double x1, double y0, double y1)
    {
        if (x1 < x0) {
            this.x0 = x1;
            this.x1 = x0;
        }
        else {
            this.x0 = x0;
            this.x1 = x1;
        }

        this.y0 = y0;
        this.y1 = y1;
    }

    @Override
    protected int floorIndexOfX(double x)
    {
        if (x == x1) return 1;
        return 0;
    }

    @Override
    protected double extrapolateLeft(double x)
    {
        return interpolate(x, leftBound(), rightBound(), getY(0), getY(1));
    }

    @Override
    protected double extrapolateRight(double x)
    {
        return extrapolateLeft(x);
    }

    @Override
    protected double interpolate(double x, int floorIndex)
    {
        return interpolate(x, getX(floorIndex), getX(floorIndex + 1), getY(floorIndex), getY(floorIndex + 1));
    }

    @Override
    public int getCount()
    {
        return 2;
    }

    @Override
    public double getX(int index)
    {
        if (index == 0) return x0;
        else if (index == 1) return x1;
        return -1;
    }

    @Override
    public double getY(int index)
    {
        if (index == 0) return y0;
        else if (index == 1) return y1;
        return -1;
    }

    @Override
    public void setY(int index, double value)
    {
        if (index == 0) y0 = value;
        else if (index == 1) y1 = value;
    }

    @Override
    public int indexOfX(double x)
    {
        if (x == x0) return 0;
        else if (x == x1) return 1;
        return -1;
    }

    @Override
    public int indexOfY(double y)
    {
        if (y == y0) return 0;
        else if (y == y1) return 1;
        return -1;
    }

    @Override
    public double leftBound()
    {
        return x0;
    }

    @Override
    public double rightBound()
    {
        return x1;
    }
}
