package ru.ssau.tk.DoubleA.javalabs.functions;

public class LinkedListTabulatedFunction extends AbstractTabulatedFunction{

    private Node head;

    private void addNode(double x, double y)
    {
        Node cur = new Node();
        cur.x = x;
        cur.y = y;

        if (head == null)
        {
            head = cur;
            head.next = cur;
            head.prev = cur;
        } else
        {
            cur.next = head;
            head.prev.next = cur;
            cur.prev = head.prev;
            head.prev = cur;
        }
        count++;
    }
    private Node getNode(int index)
    {
        Node currentNode;

        if (index < count / 2)
        {
            currentNode = head;
            int currentIndex = 0;

            while (currentIndex != index)
            {
                currentNode = currentNode.next;
                currentIndex++;
            }
        }
        else
        {
            currentNode = head.prev;
            int currentIndex = count - 1;

            while (currentIndex != index)
            {
                currentNode = currentNode.prev;
                currentIndex--;
            }
        }

        return currentNode;
    }
    private Node floorNodeOfX(double x)
    {
        Node currentNode = head, floorNode = head;
        int valuesBelowX = 0;
        double floorValue = -1;

        for (int currentIndex = 0; currentIndex < count; currentIndex++)
        {
            if (currentNode.x < x)
            {
                if (valuesBelowX == 0) {
                    floorNode = currentNode;
                    floorValue = currentNode.x;
                }
                else if (currentNode.x > floorValue) {
                    floorNode = currentNode;
                    floorValue = currentNode.x;
                }
                valuesBelowX++;
            }
            currentNode = currentNode.next;
        }

        return floorNode;
    }

    public LinkedListTabulatedFunction(double[] xValues, double[] yValues)
    {
        head = null;
        int xValuesLength = xValues.length, yValuesLength = yValues.length;

        for (int xIndex = 0, yIndex = 0;
                 xIndex < xValuesLength && yIndex < yValuesLength;
                 xIndex++, yIndex++)
        {
            this.addNode(xValues[xIndex], yValues[yIndex]);
        }
    }
    public LinkedListTabulatedFunction(MathFunction source, double xFrom, double xTo, int count)
    {
        head = null;

        if (xFrom == xTo)
        {
            for (int xIndex = 0; xIndex < count; xIndex++)
            {
                addNode(xFrom, source.apply(xFrom));
            }
        }
        else if (count >= 2)
        {
            if (xFrom > xTo)
            {
                double swapBuffer = xFrom;
                xFrom = xTo;
                xTo = swapBuffer;
            }

            final int POINTS_BEFORE_END = count - 1;
            final double INTERVAL = (xFrom - xTo) / (count - 1);

            for (int xIndex = 0; xIndex < POINTS_BEFORE_END; xIndex++)
            {
                this.addNode(xFrom + xIndex * INTERVAL, source.apply(xFrom + xIndex * INTERVAL));
            }
            this.addNode(xTo, source.apply(xTo));
        }
    }

    @Override
    public int getCount()
    {
        return count;
    }

    public double leftBound()
    {
        return head.x;
    }
    public double rightBound()
    {
        return head.prev.x;
    }

    public double getX(int index)
    {
        return getNode(index).x;
    }
    public double getY(int index)
    {
        return getNode(index).y;
    }
    public void setY(int index, double value)
    {
        getNode(index).y = value;
    }

    public int indexOfX(double x)
    {
        Node currentNode = head;
        int currentIndex = 0;

        while (currentNode.x != x && currentIndex < count)
        {
            currentNode = currentNode.next;
            currentIndex++;
        }

        if (currentIndex == count) return -1;
        return currentIndex;
    }
    public int indexOfY(double y)
    {
        Node currentNode = head;
        int currentIndex = 0;

        while (currentNode.y != y && currentIndex < count)
        {
            currentNode = currentNode.next;
            currentIndex++;
        }

        if (currentIndex == count) return -1;
        return currentIndex;
    }
    protected int floorIndexOfX(double x)
    {
        Node currentNode = head;
        int valuesBelowX = 0, floorIndex = -1;
        double floorValue = -1;

        for (int currentIndex = 0; currentIndex < count; currentIndex++)
        {
            if (currentNode.x < x)
            {
                if (valuesBelowX == 0) {
                    floorIndex = currentIndex;
                    floorValue = currentNode.x;
                }
                else if (currentNode.x > floorValue) {
                    floorIndex = currentIndex;
                    floorValue = currentNode.x;
                }
                valuesBelowX++;
            }
            currentNode = currentNode.next;
        }

        if (valuesBelowX == 0 || valuesBelowX == count) {
            return valuesBelowX;
        }
        else {
            return floorIndex;
        }

    }

    protected double extrapolateLeft(double x)
    {
        if (count == 1) {
            return x;
        }

        return interpolate(x, head.x, head.next.x, head.y, head.next.y);
    }
    protected double extrapolateRight(double x)
    {
        if (count == 1) {
            return x;
        }

        return interpolate(x, head.prev.prev.x, head.prev.x, head.prev.prev.y, head.prev.y);
    }
    protected double interpolate(double x, int floorIndex)
    {
        if (count == 1) {
            return x;
        }

        if (floorIndex == 0) {
            return extrapolateLeft(x);
        }
        else if (floorIndex == count) {
            return extrapolateRight(x);
        }
        else {
            Node floorNode = getNode(floorIndex);
            return interpolate(x, floorNode.x, floorNode.next.x, floorNode.y, floorNode.next.y);
        }
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
            if (indexOfX(x) != -1) {
                return getY(indexOfX(x));
            }
            else {
                Node floorNode = floorNodeOfX(x);
                return interpolate(x, floorNode.x, floorNode.next.x, floorNode.y, floorNode.next.y);
            }
        }
    }

}
