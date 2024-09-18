package ru.ssau.tk.DoubleA.javalabs.functions;

public interface MathFunction
{
    double apply(double x);

    default CompositeFunction andThen(MathFunction afterFunction)
    {
        return new CompositeFunction(this, afterFunction);
    }
}
