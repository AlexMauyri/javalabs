package ru.ssau.tk.DoubleA.javalabs.persistence.dto;

import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ssau.tk.DoubleA.javalabs.functions.*;
import ru.ssau.tk.DoubleA.javalabs.operations.MiddleSteppingDifferentialOperator;
import ru.ssau.tk.DoubleA.javalabs.operations.TabulatedIntegrationOperator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculationDataDTOTest {
    double[] xVal = new double[]{9, -111, 343245};
    double[] yVal = new double[]{-700, 4004, 34};
    List<List<MathFunction>> functionSets = Arrays.asList(
            Arrays.asList(
                    new SqrFunction(),
                    new IdentityFunction(),
                    new MiddleSteppingDifferentialOperator(1E-5)),
            Arrays.asList(
                    new ArrayTabulatedFunction(new SqrFunction(), 1, 10, 7),
                    new TabulatedIntegrationOperator(),
                    new IdentityFunction()),
            Arrays.asList(
                    new LinkedListTabulatedFunction(new SqrFunction(), -7, 3, 25),
                    new ArrayTabulatedFunction(new IdentityFunction(), 0, 255, 3),
                    new NRootCalculateFunction(4))
    );
    List<CalculationDataDTO> data;

    @BeforeEach
    public void setUp() {
        data = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            data.add(new CalculationDataDTO(xVal[i], yVal[i], functionSets.get(i)));
        }
    }

    @Test
    public void testGet() {
        for (int i = 0; i < 3; i++) {
            assertEquals(xVal[i], data.get(i).getAppliedValue());
            assertEquals(yVal[i], data.get(i).getResultValue());
            assertEquals(functionSets.get(i).get(i).hashCode(), data.get(i).getAppliedFunctionData().get(i).hashCode());
        }
    }
}
