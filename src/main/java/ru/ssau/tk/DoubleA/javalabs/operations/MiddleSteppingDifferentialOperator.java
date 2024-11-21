package ru.ssau.tk.DoubleA.javalabs.operations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.ssau.tk.DoubleA.javalabs.functions.MathFunction;

import java.io.Serial;

public class MiddleSteppingDifferentialOperator extends SteppingDifferentialOperator {
    @Serial
    private static final long serialVersionUID = -5045274039176809169L;

    @JsonCreator
    public MiddleSteppingDifferentialOperator(@JsonProperty("step") double step) {
        super(step);
    }

    @Override
    public MathFunction derive(MathFunction function) {
        return new MathFunction() {
            @Override
            public double apply(double x) {
                return (function.apply(x + step) - function.apply(x - step)) / (2.0 * step);
            }
        };
    }

    @Override
    public double apply(double x) {
        throw new UnsupportedOperationException();
    }
}
