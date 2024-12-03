package ru.ssau.tk.DoubleA.javalabs.ui.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ssau.tk.DoubleA.javalabs.functions.AbstractTabulatedFunction;

import java.util.Arrays;

@Getter
@Setter
@NoArgsConstructor
public class TabulatedFunctionOnArraysRequest {
    private double[] xValues;
    private double[] yValues;

    @JsonCreator
    public TabulatedFunctionOnArraysRequest(@JsonProperty(value = "xValues") double[] xValues, @JsonProperty(value = "yValues") double[] yValues) throws IllegalArgumentException {
        this.xValues = Arrays.copyOf(xValues, xValues.length);
        this.yValues = Arrays.copyOf(yValues, yValues.length);
    }
}