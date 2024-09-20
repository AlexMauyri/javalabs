package ru.ssau.tk.DoubleA.javalabs.functions;

public class LinkedListTabulatedFunction extends AbstractTabulatedFunction implements Insertable, Removable {
    static class Node {
        public Node previous, next;
        public double x, y;
    }

    private Node head;

    public LinkedListTabulatedFunction(double[] xValues, double[] yValues) {
        count = 0;
        head = null;
        int xValuesLength = xValues.length, yValuesLength = yValues.length;

        for (int xIndex = 0, yIndex = 0; xIndex < xValuesLength && yIndex < yValuesLength; xIndex++, yIndex++) {
            this.addNode(xValues[xIndex], yValues[yIndex]);
        }
    }

    public LinkedListTabulatedFunction(MathFunction source, double xFrom, double xTo, int count) {
        this.count = 0;
        head = null;

        if (xFrom == xTo || count < 2) {
            double yFrom = source.apply(xFrom);

            for (int xIndex = 0; xIndex < count; xIndex++) {
                addNode(xFrom, yFrom);
            }
        } else {
            if (xFrom > xTo) {
                double swapBuffer = xFrom;
                xFrom = xTo;
                xTo = swapBuffer;
            }

            final int PAIRS_BEFORE_END = count - 1;
            final double INTERVAL = Math.abs(xTo - xFrom) / (count - 1);

            for (int xIndex = 0; xIndex < PAIRS_BEFORE_END; xIndex++) {
                this.addNode(xFrom + xIndex * INTERVAL, source.apply(xFrom + xIndex * INTERVAL));
            }

            this.addNode(xTo, source.apply(xTo));
        }
    }

    protected void addNode(double x, double y) {
        Node cur = new Node();
        cur.x = x;
        cur.y = y;

        if (head == null) {
            head = cur;
            head.next = cur;
            head.previous = cur;
        } else {
            cur.next = head;
            head.previous.next = cur;
            cur.previous = head.previous;
            head.previous = cur;
        }

        count++;
    }

    protected Node getNode(int index) {
        Node currentNode;

        if (index < count / 2) {
            currentNode = head;
            int currentIndex = 0;

            while (currentIndex != index) {
                currentNode = currentNode.next;
                currentIndex++;
            }
        } else {
            currentNode = head.previous;
            int currentIndex = count - 1;

            while (currentIndex != index) {
                currentNode = currentNode.previous;
                currentIndex--;
            }
        }

        return currentNode;
    }

    protected Node floorNodeOfX(double x) {
        Node currentNode = head, floorNode = head;

        for (int index = 0; currentNode.x < x && index < count; index++) {
            floorNode = currentNode;
            currentNode = currentNode.next;
        }

        return floorNode;
    }

    @Override
    public void insert(double x, double y) {
        if (head == null) addNode(x, y);
        else {
            Node currentNode = head;

            while (currentNode.x < x && currentNode.next != head) {
                currentNode = currentNode.next;
            }

            // In case when currentNode == head.previous
            if (currentNode.x < x) {
                currentNode = currentNode.next;
            }

            if (currentNode.x == x) {
                currentNode.y = y;
            } else {
                Node newNode = new Node();
                newNode.x = x;
                newNode.y = y;
                newNode.next = currentNode;
                newNode.previous = currentNode.previous;
                currentNode.previous = newNode;
                newNode.previous.next = newNode;

                if (currentNode == head && newNode.x < currentNode.x) head = newNode;
            }
        }
        ++count;
    }

    @Override
    public void remove(int index) {
        if (index < count && index >= 0) {

            if (count == 1) {
                head = null;
            } else {
                Node nodeToRemove = getNode(index);
                if (index == 0) head = nodeToRemove.next;
                nodeToRemove.previous.next = nodeToRemove.next;
                nodeToRemove.next.previous = nodeToRemove.previous;
            }

            count--;
        }
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public double leftBound() {
        return head.x;
    }

    @Override
    public double rightBound() {
        return head.previous.x;
    }

    @Override
    public double getX(int index) {
        return getNode(index).x;
    }

    @Override
    public double getY(int index) {
        return getNode(index).y;
    }

    @Override
    public void setY(int index, double value) {
        getNode(index).y = value;
    }

    @Override
    public int indexOfX(double x) {
        Node currentNode = head;
        int currentIndex = 0;

        while (currentNode.x != x && currentIndex < count) {
            currentNode = currentNode.next;
            currentIndex++;
        }

        if (currentIndex == count) return -1;
        return currentIndex;
    }

    @Override
    public int indexOfY(double y) {
        Node currentNode = head;
        int currentIndex = 0;

        while (currentNode.y != y && currentIndex < count) {
            currentNode = currentNode.next;
            currentIndex++;
        }

        if (currentIndex == count) return -1;
        return currentIndex;
    }

    @Override
    protected int floorIndexOfX(double x) {
        Node currentNode = head;
        int floorIndex = 0;

        for (int curIndex = 0; currentNode.x < x && curIndex < count; curIndex++) {
            floorIndex = curIndex;
            currentNode = currentNode.next;
        }

        return (floorIndex == count - 1 && currentNode.x < x ? count : floorIndex);
    }

    @Override
    protected double extrapolateLeft(double x) {
        if (count == 1) {
            return x;
        }

        return interpolate(x, head.x, head.next.x, head.y, head.next.y);
    }

    @Override
    protected double extrapolateRight(double x) {
        if (count == 1) {
            return x;
        }

        return interpolate(x, head.previous.previous.x, head.previous.x, head.previous.previous.y, head.previous.y);
    }

    @Override
    protected double interpolate(double x, int floorIndex) {
        if (count == 1) {
            return x;
        }

        if (floorIndex == 0) {
            return extrapolateLeft(x);
        } else if (floorIndex >= count - 1) {
            return extrapolateRight(x);
        } else {
            Node floorNode = getNode(floorIndex);
            return interpolate(x, floorNode.x, floorNode.next.x, floorNode.y, floorNode.next.y);
        }
    }

    @Override
    public double apply(double x) {
        if (x < leftBound()) {
            return extrapolateLeft(x);
        } else if (x > rightBound()) {
            return extrapolateRight(x);
        } else {
            int indexX = indexOfX(x);

            // If x already exists return y, else interpolate value of y
            if (indexX != -1) {
                return getY(indexX);
            } else {
                if (count == 1) {
                    return x;
                }
                Node floorNode = floorNodeOfX(x);
                return interpolate(x, floorNode.x, floorNode.next.x, floorNode.y, floorNode.next.y);
            }
        }
    }
}
