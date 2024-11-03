package ru.ssau.tk.DoubleA.javalabs.persistence;

import ru.ssau.tk.DoubleA.javalabs.functions.*;
import ru.ssau.tk.DoubleA.javalabs.operations.MiddleSteppingDifferentialOperator;
import ru.ssau.tk.DoubleA.javalabs.operations.TabulatedIntegrationOperator;
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

//        List<MathFunction> list = Arrays.asList(
//                tabulatedFunction,
//                sqrFunction,
//                differentialOperator);
//        System.out.println(calculationService.computeHash(list));
//
//        Calculation calculation = calculationService.findCalculation(x, list);
//        if (calculation == null) {
//            calculationService.addCalculationRoute(x, y, list);
//            calculation = calculationService.findCalculation(x, list);
//        }
//
//        CalculationDataDTO data = calculationService.getCalculationRoute(calculation.getId());
//        System.out.println(data.getAppliedValue());
//        System.out.println(data.getResultValue());
//        for (MathFunction mf : data.getAppliedFunctionData()) {
//            System.out.println(mf.getClass().getName());
//        }
//        NRootCalculateFunction nRootCalculateFunction = new NRootCalculateFunction(5);
//        finalFunction = differentialOperator.derive(new CompositeFunction(tabulatedFunction, nRootCalculateFunction));
//        List<MathFunction> list = List.of(
//                nRootCalculateFunction,
//                tabulatedFunction,
//                differentialOperator
//        );
//        calculationService.addCalculationRoute(2.4, finalFunction.apply(2.4), list);

//        List<MathFunction> list = Arrays.asList(sqrFunction);
//        calculationService.addCalculationRoute(2, 4, list);
//        calculationService.addCalculationRoute(1, 1, list);
//        calculationService.addCalculationRoute(5, 25, list);
//        calculationService.addCalculationRoute(13, 169, list);
//        calculationService.addCalculationRoute(4, 16, list);

        System.out.println(calculationService.getAllCalculations(75, y, Operations.lessOrEqual, Operations.lessThen, Sorting.ASCENDING, Sorting.DESCENDING));
    }
}
