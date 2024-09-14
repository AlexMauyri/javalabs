package ru.ssau.tk.DoubleA.javalabs.functions;

public class DerivativeFunction implements MathFunction {

    private final static double epsilon = Math.pow(1, -20);
    private MathFunction mathFunction;

    DerivativeFunction(MathFunction mathFunction) {
        this.mathFunction = mathFunction;
    }

    @Override
    public double apply(double x) {
        return (mathFunction.apply(x + epsilon) - mathFunction.apply(x - epsilon)) / (2.0 * epsilon);
    }

    public void setMathFunction(MathFunction mathFunction) {
        this.mathFunction = mathFunction;
    }
}
