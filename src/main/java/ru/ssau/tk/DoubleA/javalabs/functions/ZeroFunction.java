package ru.ssau.tk.DoubleA.javalabs.functions;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.io.Serial;

public class ZeroFunction extends ConstantFunction {
    @Serial
    private static final long serialVersionUID = -1197221142586677910L;

    @JsonCreator
    public ZeroFunction() {
        super(0);
    }
}
