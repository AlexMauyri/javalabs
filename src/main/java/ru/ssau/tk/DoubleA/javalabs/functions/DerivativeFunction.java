package ru.ssau.tk.DoubleA.javalabs.functions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;

public class DerivativeFunction implements MathFunction {
    @Serial
    private static final long serialVersionUID = 4355001412073990461L;
    private final static double EPSILON = 1E-5;
    private MathFunction mathFunction;

    @JsonCreator
    public DerivativeFunction(@JsonProperty(value = "mathFunction") MathFunction mathFunction) {
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
