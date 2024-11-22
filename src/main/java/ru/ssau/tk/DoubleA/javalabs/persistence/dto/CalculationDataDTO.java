package ru.ssau.tk.DoubleA.javalabs.persistence.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.ssau.tk.DoubleA.javalabs.functions.MathFunction;

import java.util.List;

public class CalculationDataDTO {
    private final double appliedValue;
    private final double resultValue;
    private final List<MathFunction> appliedFunctionData;

    @JsonCreator
    public CalculationDataDTO(@JsonProperty(value = "appliedValue") double appliedValue, @JsonProperty(value = "resultValue") double resultValue, @JsonProperty(value = "appliedFunctionData") List<MathFunction> appliedFunctionData) {
        this.appliedValue = appliedValue;
        this.resultValue = resultValue;
        this.appliedFunctionData = appliedFunctionData;
    }

    public double getAppliedValue() {
        return appliedValue;
    }

    public double getResultValue() {
        return resultValue;
    }

    public List<MathFunction> getAppliedFunctionData() {
        return appliedFunctionData;
    }

    @Override
    public String toString() {
        return "CalculationDataDTO{" +
                "appliedValue=" + appliedValue +
                ", resultValue=" + resultValue +
                '}';
    }
}
