package ru.ssau.tk.DoubleA.javalabs.operations;

import ru.ssau.tk.DoubleA.javalabs.functions.MathFunction;

public interface DifferentialOperator<T> extends MathFunction {
    T derive (T function);
}
