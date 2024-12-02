package ru.ssau.tk.DoubleA.javalabs.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TabulatedFunctionOnArraysRequest {
    private double[] x;
    private double[] y;
}