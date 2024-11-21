package ru.ssau.tk.DoubleA.javalabs.functions;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public interface MathFunction extends Serializable {
    double apply(double x);

    default CompositeFunction andThen(MathFunction afterFunction) {
        return new CompositeFunction(this, afterFunction);
    }
}
