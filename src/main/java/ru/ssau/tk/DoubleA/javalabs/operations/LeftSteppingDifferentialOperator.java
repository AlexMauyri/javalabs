package ru.ssau.tk.DoubleA.javalabs.operations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.ssau.tk.DoubleA.javalabs.functions.MathFunction;

import java.io.Serial;

public class LeftSteppingDifferentialOperator extends SteppingDifferentialOperator {
    @Serial
    private static final long serialVersionUID = 7629988822199654720L;

    @JsonCreator
    public LeftSteppingDifferentialOperator(@JsonProperty(value = "step") double step) {
        super(step);
    }

    @Override
    public MathFunction derive(MathFunction function) {
        return new MathFunction() {
            @Override
            public double apply(double x) {
                return (function.apply(x) - function.apply(x - step)) / step;
            }
        };
    }

    @Override
    public double apply(double x) {
        throw new UnsupportedOperationException();
    }
}
