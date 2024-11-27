package ru.ssau.tk.DoubleA.javalabs.ui.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TabulatedFunctionRequest {
    private double[] x;
    private double[] y;
}