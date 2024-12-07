package ru.ssau.tk.DoubleA.javalabs.functions;

import ru.ssau.tk.DoubleA.javalabs.annotation.SimpleFunction;

import java.io.Serial;

@SimpleFunction(localizedName = "Квадратичная функция")
public class SqrFunction implements MathFunction {
    @Serial
    private static final long serialVersionUID = -106292742190894331L;

    @Override
    public double apply(double x) {
        return Math.pow(x, 2);
    }

    @Override
    public int hashCode() {
        return 621939904;
    }
}