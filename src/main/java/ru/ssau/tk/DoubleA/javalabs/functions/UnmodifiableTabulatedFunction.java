package ru.ssau.tk.DoubleA.javalabs.functions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.util.Iterator;

public class UnmodifiableTabulatedFunction implements TabulatedFunction {
    @Serial
    private static final long serialVersionUID = 687593488411140331L;
    TabulatedFunction tabulatedFunction;

    @JsonCreator
    public UnmodifiableTabulatedFunction(@JsonProperty(value = "tabulatedFunction") TabulatedFunction tabulatedFunction) {
        this.tabulatedFunction = tabulatedFunction;
    }

    @Override
    public int getCount() {
        return tabulatedFunction.getCount();
    }

    @Override
    public double getX(int index) {
        return tabulatedFunction.getX(index);
    }

    @Override
    public double getY(int index) {
        return tabulatedFunction.getY(index);
    }

    @Override
    public void setY(int index, double value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOfX(double x) {
        return tabulatedFunction.indexOfX(x);
    }

    @Override
    public int indexOfY(double y) {
        return tabulatedFunction.indexOfY(y);
    }

    @Override
    public double leftBound() {
        return tabulatedFunction.leftBound();
    }

    @Override
    public double rightBound() {
        return tabulatedFunction.rightBound();
    }

    @Override
    public Iterator<Point> iterator() {
        return tabulatedFunction.iterator();
    }

    @Override
    public double apply(double x) {
        return tabulatedFunction.apply(x);
    }

    @Override
    public int hashCode() {
        return tabulatedFunction.hashCode();
    }
}
