package ru.ssau.tk.DoubleA.javalabs.functions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.util.List;
import java.util.zip.CRC32;

public class CompositeFunction implements MathFunction {
    @Serial
    private static final long serialVersionUID = 2280350949663462697L;
    private final MathFunction firstFunction;
    private final MathFunction secondFunction;

    @JsonCreator
    public CompositeFunction(@JsonProperty(value = "firstFunction") MathFunction firstFunction, @JsonProperty(value = "secondFunction") MathFunction secondFunction) {
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

    public static CompositeFunction createCompositeFunctionFromList(List<MathFunction> functionList) {
        CompositeFunction function = new CompositeFunction(functionList.get(0), functionList.get(1));
        for (int i = 2; i < functionList.size(); i++) {
            function = new CompositeFunction(function, functionList.get(i));
        }
        return function;
    }
}