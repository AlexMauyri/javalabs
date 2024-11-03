package ru.ssau.tk.DoubleA.javalabs.persistence.dto;

import ru.ssau.tk.DoubleA.javalabs.functions.MathFunction;

import java.util.List;

public class CalculationDataDTO {
    private final double appliedValue;
    private final double resultValue;
    private final List<MathFunction> appliedFunctionData;

    public CalculationDataDTO(double appliedValue, double resultValue, List<MathFunction> appliedFunctionData) {
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
}
