package ru.ssau.tk.DoubleA.javalabs.functions;

import java.lang.Math;

public class SqrFunction implements MathFunction {

    @Override
    public double apply(double x) {
        return Math.pow(x, 2);
    }
}