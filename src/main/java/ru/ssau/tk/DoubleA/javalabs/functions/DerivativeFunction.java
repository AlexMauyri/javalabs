package ru.ssau.tk.DoubleA.javalabs.functions;

import java.io.Serial;

public class DerivativeFunction implements MathFunction {
    @Serial
    private static final long serialVersionUID = 4355001412073990461L;
    private final static double EPSILON = 1E-5;
    private MathFunction mathFunction;

    public DerivativeFunction(MathFunction mathFunction) {
        this.mathFunction = mathFunction;
    }

    @Override
    public double apply(double x) {
        return (mathFunction.apply(x + EPSILON) - mathFunction.apply(x - EPSILON)) / (2.0 * EPSILON);
    }

    public void setMathFunction(MathFunction mathFunction) {
        this.mathFunction = mathFunction;
    }

    @Override
    public int hashCode() {
        return mathFunction.hashCode();
    }
}
