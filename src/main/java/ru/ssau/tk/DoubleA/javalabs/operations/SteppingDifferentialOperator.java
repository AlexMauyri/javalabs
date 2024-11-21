package ru.ssau.tk.DoubleA.javalabs.operations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.ssau.tk.DoubleA.javalabs.functions.MathFunction;

import java.io.Serial;

public abstract class SteppingDifferentialOperator implements DifferentialOperator<MathFunction> {
    @Serial
    private static final long serialVersionUID = -741690813693932136L;
    protected double step;

    @JsonCreator
    public SteppingDifferentialOperator(@JsonProperty("step") double step) {
        if (step <= 0  || Double.isInfinite(step) || Double.isNaN(step)) {
            throw new IllegalArgumentException();
        }
        this.step = step;
    }

    public double getStep() {
        return step;
    }

    public void setStep(double step) {
        if (step <= 0  || Double.isInfinite(step) || Double.isNaN(step)) {
            throw new IllegalArgumentException();
        }
        this.step = step;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(step);
    }
}
