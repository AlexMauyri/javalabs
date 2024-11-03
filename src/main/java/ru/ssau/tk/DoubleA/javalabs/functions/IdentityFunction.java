package ru.ssau.tk.DoubleA.javalabs.functions;

import java.io.Serial;

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