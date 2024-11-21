package ru.ssau.tk.DoubleA.javalabs.functions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Point {
    public final double x;
    public final double y;

    @JsonCreator
    public Point(@JsonProperty(value = "x") double x, @JsonProperty(value = "y") double y) {
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
        final double EPSILON = 1E-10;
        return Math.abs(this.x - point.x) < EPSILON && Math.abs(this.y - point.y) < EPSILON;
    }
}
