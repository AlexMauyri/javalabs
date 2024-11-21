package ru.ssau.tk.DoubleA.javalabs.functions;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.io.Serial;

public class UnitFunction extends ConstantFunction {

    @Serial
    private static final long serialVersionUID = 2839229684747388652L;

    @JsonCreator
    public UnitFunction() {
        super(1);
    }
}
