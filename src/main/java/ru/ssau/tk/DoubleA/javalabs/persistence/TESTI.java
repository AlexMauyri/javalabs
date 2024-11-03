package ru.ssau.tk.DoubleA.javalabs.persistence;

import ru.ssau.tk.DoubleA.javalabs.functions.ArrayTabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.CompositeFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.MathFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.SqrFunction;
import ru.ssau.tk.DoubleA.javalabs.operations.MiddleSteppingDifferentialOperator;
import ru.ssau.tk.DoubleA.javalabs.persistence.dto.CalculationDataDTO;
import ru.ssau.tk.DoubleA.javalabs.persistence.entity.Calculation;

import java.util.Arrays;
import java.util.List;

public class TESTI {
    static double x = 5;

    public static void main(String[] args) {
        CalculationService calculationService = new CalculationService();

        ArrayTabulatedFunction tabulatedFunction = new ArrayTabulatedFunction(new SqrFunction(), 1, 10, 100);
        SqrFunction sqrFunction = new SqrFunction();
        MiddleSteppingDifferentialOperator differentialOperator = new MiddleSteppingDifferentialOperator(1E-5);
        MathFunction finalFunction = differentialOperator.derive(new CompositeFunction(tabulatedFunction, sqrFunction));
        double y = finalFunction.apply(x);

        List<MathFunction> list = Arrays.asList(
                tabulatedFunction,
                sqrFunction,
                differentialOperator);
        System.out.println(calculationService.computeHash(list));

        Calculation calculation = calculationService.findCalculation(x, list);
        if (calculation == null) {
            calculationService.addCalculationRoute(x, y, list);
            calculation = calculationService.findCalculation(x, list);
        }

        CalculationDataDTO data = calculationService.getCalculationRoute(calculation.getId());
        System.out.println(data.getAppliedValue());
        System.out.println(data.getResultValue());
        for (MathFunction mf : data.getAppliedFunctionData()) {
            System.out.println(mf.getClass().getName());
        }
    }
}
