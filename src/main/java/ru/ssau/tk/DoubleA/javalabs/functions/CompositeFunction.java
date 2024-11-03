package ru.ssau.tk.DoubleA.javalabs.functions;

import java.io.Serial;
import java.util.zip.CRC32;

public class CompositeFunction implements MathFunction {
    @Serial
    private static final long serialVersionUID = 2280350949663462697L;
    private final MathFunction firstFunction;
    private final MathFunction secondFunction;

    public CompositeFunction(MathFunction firstFunction, MathFunction secondFunction) {
        this.firstFunction = firstFunction;
        this.secondFunction = secondFunction;
    }

    @Override
    public double apply(double x) {
        return secondFunction.apply(firstFunction.apply(x));
    }

    @Override
    public int hashCode() {
        CRC32 crc = new CRC32();
        crc.update(firstFunction.hashCode());
        crc.update(secondFunction.hashCode());
        return (int) crc.getValue();
    }
}