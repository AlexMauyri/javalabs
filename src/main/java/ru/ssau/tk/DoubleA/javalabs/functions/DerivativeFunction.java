package ru.ssau.tk.DoubleA.javalabs.functions;

public class DerivativeFunction implements MathFunction {

    private final static double EPSILON = 1E-5;
    private MathFunction mathFunction;

    DerivativeFunction(MathFunction mathFunction) {
        this.mathFunction = mathFunction;
    }

    @Override
    public double apply(double x) {
        return (mathFunction.apply(x + EPSILON) - mathFunction.apply(x - EPSILON)) / (2.0 * EPSILON);
    }

    public void setMathFunction(MathFunction mathFunction) {
        this.mathFunction = mathFunction;
    }
}
