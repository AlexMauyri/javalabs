package ru.ssau.tk.DoubleA.javalabs.exceptions;

import java.io.Serial;

public class FunctionAlreadyExists extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 963332985103947808L;

    public FunctionAlreadyExists(String message) {
        super(message);
    }
}
