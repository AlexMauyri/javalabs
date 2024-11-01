package ru.ssau.tk.DoubleA.javalabs.functions;

import java.io.Serializable;

public interface MathFunction extends Serializable {
    double apply(double x);

    default CompositeFunction andThen(MathFunction afterFunction) {
        return new CompositeFunction(this, afterFunction);
    }
}
