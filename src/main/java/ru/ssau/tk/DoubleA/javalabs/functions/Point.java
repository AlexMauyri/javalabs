package ru.ssau.tk.DoubleA.javalabs.functions;

public class Point {
    public final double x;
    public final double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final Point point = (Point) obj;

        return this.x == point.x && this.y == point.y;
    }
}
