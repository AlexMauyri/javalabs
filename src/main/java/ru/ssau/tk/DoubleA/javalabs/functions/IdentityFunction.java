package ru.ssau.tk.DoubleA.javalabs.functions;

import ru.ssau.tk.DoubleA.javalabs.ui.annotation.SimpleFunction;

import java.io.Serial;

@SimpleFunction(localizedName = "Тождественная функция")
public class IdentityFunction implements MathFunction {
    @Serial
    private static final long serialVersionUID = 6935007519564659857L;

    @Override
    public double apply(double x) {
        return x;
    }

    @Override
    public int hashCode() {
        return 329232770;
    }
}