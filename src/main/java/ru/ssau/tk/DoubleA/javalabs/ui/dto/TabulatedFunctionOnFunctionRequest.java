package ru.ssau.tk.DoubleA.javalabs.ui.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TabulatedFunctionOnFunctionRequest {
    private String functionName;
    private double from;
    private double to;
    private int amountOfPoints;
}
