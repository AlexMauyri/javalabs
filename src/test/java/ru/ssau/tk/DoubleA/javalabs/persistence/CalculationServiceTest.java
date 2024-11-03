package ru.ssau.tk.DoubleA.javalabs.persistence;

import ru.ssau.tk.DoubleA.javalabs.functions.*;
import ru.ssau.tk.DoubleA.javalabs.operations.MiddleSteppingDifferentialOperator;
import ru.ssau.tk.DoubleA.javalabs.operations.TabulatedIntegrationOperator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ssau.tk.DoubleA.javalabs.persistence.dao.CalculationDAOImpl;
import ru.ssau.tk.DoubleA.javalabs.persistence.dto.CalculationDataDTO;
import ru.ssau.tk.DoubleA.javalabs.persistence.entity.Calculation;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculationServiceTest {
    private final CalculationDAOImpl calculationDAO = new CalculationDAOImpl();
    //private final List<Calculation> createdCalculations = new ArrayList<>();
    private final CalculationService calculationService = new CalculationService();

    private final double[] xVal = new double[]{9, -111, 343245};
    private final double[] yVal = new double[]{-700, 4004, 34};
    private final List<List<MathFunction>> functionSets = Arrays.asList(
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

    @BeforeEach
    public void setUp() {
        for (int i = 0; i < 3; i++) {
            calculationService.addCalculationRoute(xVal[i], yVal[i], functionSets.get(i));
        }
    }

    @AfterEach
    public void tearDown() {
        for (int i = 0; i < 3; i++) {
            calculationDAO.delete(calculationService.findCalculation(xVal[i], functionSets.get(i)).getId());
        }
    }

    @Test
    public void testFindAndHash() {
        for (int i = 0; i < 3; i++) {
            Calculation calculation = calculationService.findCalculation(xVal[i], functionSets.get(i));
            assertEquals(xVal[i], calculation.getAppliedX());
            assertEquals(yVal[i], calculation.getResultY());
            assertEquals(calculationService.computeHash(functionSets.get(i)), calculation.getHash());
        }
    }

    @Test
    public void testGetCalculationRoute() {
        for (int i = 0; i < 3; i++) {
            Calculation calculation = calculationService.findCalculation(xVal[i], functionSets.get(i));
            CalculationDataDTO data = calculationService.getCalculationRoute(calculation.getId());
            assertEquals(xVal[i], data.getAppliedValue());
            assertEquals(yVal[i], data.getResultValue());

            for (int j = 0; j < 3; j++) {
                assertEquals(functionSets.get(i).get(j).getClass().getSimpleName(), data.getAppliedFunctionData().get(j).getClass().getSimpleName());
            }
        }
    }
}
