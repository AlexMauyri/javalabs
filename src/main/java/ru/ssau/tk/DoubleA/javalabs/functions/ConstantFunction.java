package ru.ssau.tk.DoubleA.javalabs.functions;

import java.io.Serial;

public class ConstantFunction implements MathFunction {
    @Serial
    private static final long serialVersionUID = 7180419822453563326L;
    private final double CONSTANT;

    public ConstantFunction(double CONSTANT) {
        this.CONSTANT = CONSTANT;
    }

    @Override
    public double apply(double x) {
        return CONSTANT;
    }

    public double getCONSTANT() {
        return CONSTANT;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(CONSTANT);
    }
}
