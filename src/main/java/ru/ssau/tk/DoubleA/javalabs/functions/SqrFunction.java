package ru.ssau.tk.DoubleA.javalabs.functions;

import java.io.Serial;

public class SqrFunction implements MathFunction {
    @Serial
    private static final long serialVersionUID = -106292742190894331L;

    @Override
    public double apply(double x) {
        return Math.pow(x, 2);
    }

    @Override
    public int hashCode() {
        return 621939904;
    }
}