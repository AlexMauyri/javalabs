package ru.ssau.tk.DoubleA.javalabs.functions;

import ru.ssau.tk.DoubleA.javalabs.exceptions.InterpolationException;

import java.io.Serial;
import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedListTabulatedFunction extends AbstractTabulatedFunction implements Insertable, Removable, Serializable {
    @Serial
    private static final long serialVersionUID = -6245561391336153225L;

    private Node head;

    public LinkedListTabulatedFunction(double[] xValues, double[] yValues) throws IllegalArgumentException {
        if (xValues.length < 2 || yValues.length < 2) {
            throw new IllegalArgumentException("Count of List Tabulated Function nodes cannot be less than 2");
        }

        AbstractTabulatedFunction.checkLengthIsTheSame(xValues, yValues);
        AbstractTabulatedFunction.checkSorted(xValues);

        count = 0;
        head = null;
        int xValuesLength = xValues.length;

        for (int index = 0; index < xValuesLength; index++) {
            this.addNode(xValues[index], yValues[index]);
        }
    }

    public LinkedListTabulatedFunction(MathFunction source, double xFrom, double xTo, int count) throws IllegalArgumentException {
        if (count < 2) {
            throw new IllegalArgumentException("Count of List Tabulated Function nodes cannot be less than 2");
        }

        this.count = 0;
        head = null;

        if (xFrom == xTo) {
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

    protected Node getNode(int index) throws IllegalArgumentException {
        if (index < 0) {
            throw new IllegalArgumentException("Index cannot be less than zero");
        } else if (index >= count) {
            throw new IllegalArgumentException("Index cannot be greater than or equal to count");
        }

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
    public void remove(int index) throws IllegalArgumentException, IllegalStateException {
        if (index < 0) {
            throw new IllegalArgumentException("Index cannot be less than zero");
        } else if (index >= count) {
            throw new IllegalArgumentException("Index cannot be greater than or equal to count");
        } else if (count == 2) {
            throw new IllegalStateException("Count of List Tabulated Function nodes cannot be less than 2");
        }

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

    @Override
    public Iterator<Point> iterator() throws NoSuchElementException {
        return new Iterator<>() {
            private Node currentNode = head;
            private int internalCount = 0;

            @Override
            public boolean hasNext() {
                return internalCount < count;
            }

            @Override
            public Point next() {
                if (currentNode == null) throw new NoSuchElementException();

                Point point = new Point(currentNode.x, currentNode.y);
                ++internalCount;

                if (hasNext()) {
                    currentNode = currentNode.next;
                } else {
                    currentNode = null;
                }
                return point;
            }
        };
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
    public double getX(int index) throws IllegalArgumentException {
        if (index < 0) {
            throw new IllegalArgumentException("Index cannot be less than zero");
        } else if (index >= count) {
            throw new IllegalArgumentException("Index cannot be greater than or equal to count");
        }

        return getNode(index).x;
    }

    @Override
    public double getY(int index) throws IllegalArgumentException {
        if (index < 0) {
            throw new IllegalArgumentException("Index cannot be less than zero");
        } else if (index >= count) {
            throw new IllegalArgumentException("Index cannot be greater than or equal to count");
        }

        return getNode(index).y;
    }

    @Override
    public void setY(int index, double value) throws IllegalArgumentException {
        if (index < 0) {
            throw new IllegalArgumentException("Index cannot be less than zero");
        } else if (index >= count) {
            throw new IllegalArgumentException("Index cannot be greater than or equal to count");
        }

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
    protected int floorIndexOfX(double x) throws IllegalArgumentException {
        if (x < leftBound()) {
            throw new IllegalArgumentException("Index cannot be less than left bound");
        }

        Node currentNode = head;
        int floorIndex = 0;

        for (int curIndex = 0; currentNode.x <= x && curIndex < count; curIndex++) {
            floorIndex = curIndex;
            currentNode = currentNode.next;
        }

        return (floorIndex == count - 1 && currentNode.x < x ? count : floorIndex);
    }

    protected Node floorNodeOfX(double x) throws IllegalArgumentException {
        if (x < leftBound()) {
            throw new IllegalArgumentException("Index cannot be less than left bound");
        }

        Node currentNode = head, floorNode = head;

        for (int index = 0; currentNode.x <= x && index < count; index++) {
            floorNode = currentNode;
            currentNode = currentNode.next;
        }

        return floorNode;
    }

    @Override
    protected double extrapolateLeft(double x) {
        if (head.x == head.previous.x) {
            return head.y;
        }

        return interpolate(x, head.x, head.next.x, head.y, head.next.y);
    }

    @Override
    protected double extrapolateRight(double x) {
        if (head.x == head.previous.x) {
            return head.y;
        }

        return interpolate(x, head.previous.previous.x, head.previous.x, head.previous.previous.y, head.previous.y);
    }

    @Override
    protected double interpolate(double x, int floorIndex) throws IllegalArgumentException, InterpolationException {
        if (floorIndex < 0) {
            throw new IllegalArgumentException("Floor Index cannot be less than zero");
        } else if (floorIndex >= count - 1) {
            throw new IllegalArgumentException("Floor Index cannot be greater than or equal to last node index");
        }

        if (head.x == head.previous.x) {
            return head.y;
        }

        if (!(x > getX(floorIndex) && x < getX(floorIndex + 1))) {
            throw new InterpolationException();
        }

        Node floorNode = getNode(floorIndex);
        return interpolate(x, floorNode.x, floorNode.next.x, floorNode.y, floorNode.next.y);
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

    @Override
    public int hashCode() {
        return hashNode(head);
    }

    private int hashNode(Node head) {
        int result = 1;
        int index = 0;
        for (int i = 0; i < count; i++) {
            long xBits = Double.doubleToLongBits(head.x);
            long yBits = Double.doubleToLongBits(head.y);
            result = 31 * result + index++;
            result = 31 * result + Long.hashCode(xBits) ^ (Long.hashCode(yBits) << 16);
            head = head.next;
        }
        return result;
    }

    static class Node implements Serializable{
        @Serial
        private static final long serialVersionUID = 7621550250080520168L;
        public Node previous, next;
        public double x, y;
    }
}
