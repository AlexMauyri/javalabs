package ru.ssau.tk.DoubleA.javalabs.persistence;

import ru.ssau.tk.DoubleA.javalabs.functions.ArrayTabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.CompositeFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.MathFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.SqrFunction;
import ru.ssau.tk.DoubleA.javalabs.operations.MiddleSteppingDifferentialOperator;

import java.util.Arrays;

public class TESTI {
    static double[] xValues = {-20, -16, -9, -6, -2};
    static double[] yValues = {400, 256, 81, 36, 4};
    static double x = 5;

    public static void main(String[] args) {
        CalculationService calculationService = new CalculationService();

        ArrayTabulatedFunction tabulatedFunction = new ArrayTabulatedFunction(new SqrFunction(), 1, 10, 100);
        SqrFunction sqrFunction = new SqrFunction();
        MiddleSteppingDifferentialOperator differentialOperator = new MiddleSteppingDifferentialOperator(1E-5);
        MathFunction finalFunction = differentialOperator.derive(new CompositeFunction(tabulatedFunction, sqrFunction));
        double y = finalFunction.apply(x);

        calculationService.addCalculationRoute(x, y, Arrays.asList(
                tabulatedFunction,
                sqrFunction,
                differentialOperator));
    }
}
