package ru.ssau.tk.DoubleA.javalabs.persistence.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.ssau.tk.DoubleA.javalabs.bootloader.MathApplication;
import ru.ssau.tk.DoubleA.javalabs.functions.*;
import ru.ssau.tk.DoubleA.javalabs.operations.MiddleSteppingDifferentialOperator;
import ru.ssau.tk.DoubleA.javalabs.operations.TabulatedIntegrationOperator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ssau.tk.DoubleA.javalabs.persistence.Operations;
import ru.ssau.tk.DoubleA.javalabs.persistence.Sorting;
import ru.ssau.tk.DoubleA.javalabs.persistence.dao.CalculationGenericDAOImpl;
import ru.ssau.tk.DoubleA.javalabs.persistence.dto.CalculationDataDTO;
import ru.ssau.tk.DoubleA.javalabs.persistence.entity.Calculation;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = MathApplication.class)
@Transactional
public class CalculationRouteServiceTest {

    @Autowired
    private CalculationGenericDAOImpl calculationDAO;

    @Autowired
    private CalculationRouteService calculationRouteService;

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
            calculationRouteService.create(xVal[i], yVal[i], functionSets.get(i));
        }
    }

    @AfterEach
    public void tearDown() {
        for (int i = 0; i < 3; i++) {
            calculationDAO.delete(calculationRouteService.getByAppliedValueAndRoute(xVal[i], functionSets.get(i)).getId());
        }
    }

    @Test
    void testFiltering() {
        List<CalculationDataDTO> list = calculationRouteService.getAllByFilter(3.0,
                null,
                Operations.lessOrEqual,
                null,
                null,
                null);

        assertEquals(1, list.size());
        assertEquals(-111, list.getFirst().getAppliedValue());
        assertEquals(4004, list.getFirst().getResultValue());
        list = calculationRouteService.getAllByFilter(3.0,
                null,
                Operations.greaterThen,
                null,
                null,
                null);

        assertEquals(2, list.size());
        assertEquals(9, list.getFirst().getAppliedValue());
        assertEquals(-700, list.getFirst().getResultValue());
        assertEquals(343245, list.get(1).getAppliedValue());
        assertEquals(34, list.get(1).getResultValue());

        list = calculationRouteService.getAllByFilter(-120.0,
                null,
                Operations.lessThen,
                null,
                null,
                null);

        assertEquals(0, list.size());

        list = calculationRouteService.getAllByFilter(8.0,
                1.0,
                Operations.greaterThen,
                Operations.greaterThen,
                null,
                null);

        assertEquals(1, list.size());
        assertEquals(343245, list.getFirst().getAppliedValue());
        assertEquals(34, list.getFirst().getResultValue());

        list = calculationRouteService.getAllByFilter(-111.0,
                4004.0,
                Operations.equal,
                Operations.equal,
                null,
                null);

        assertEquals(1, list.size());
        assertEquals(-111, list.getFirst().getAppliedValue());
        assertEquals(4004, list.getFirst().getResultValue());
    }

    @Test
    void testSorting() {
        List<CalculationDataDTO> list = calculationRouteService.getAllByFilter(null,
                null,
                null,
                null,
                Sorting.ASCENDING,
                null);

        assertEquals(-111, list.getFirst().getAppliedValue());
        assertEquals(9, list.get(1).getAppliedValue());
        assertEquals(343245, list.get(2).getAppliedValue());

        list = calculationRouteService.getAllByFilter(null,
                null,
                null,
                null,
                Sorting.DESCENDING,
                null);

        assertEquals(343245, list.getFirst().getAppliedValue());
        assertEquals(9, list.get(1).getAppliedValue());
        assertEquals(-111, list.get(2).getAppliedValue());

        list = calculationRouteService.getAllByFilter(null,
                null,
                null,
                null,
                null,
                Sorting.ASCENDING);

        assertEquals(-700, list.getFirst().getResultValue());
        assertEquals(34, list.get(1).getResultValue());
        assertEquals(4004, list.get(2).getResultValue());

        list = calculationRouteService.getAllByFilter(null,
                null,
                null,
                null,
                null,
                Sorting.DESCENDING);

        assertEquals(4004, list.getFirst().getResultValue());
        assertEquals(34, list.get(1).getResultValue());
        assertEquals(-700, list.get(2).getResultValue());
    }

    @Test
    public void testFindAndHash() {
        for (int i = 0; i < 3; i++) {
            Calculation calculation = calculationRouteService.getByAppliedValueAndRoute(xVal[i], functionSets.get(i));
            assertEquals(xVal[i], calculation.getAppliedX());
            assertEquals(yVal[i], calculation.getResultY());
            assertEquals(calculationRouteService.computeHash(functionSets.get(i)), calculation.getHash());
        }
    }

    @Test
    public void testGetByCalculationId() {
        for (int i = 0; i < 3; i++) {
            Calculation calculation = calculationRouteService.getByAppliedValueAndRoute(xVal[i], functionSets.get(i));
            CalculationDataDTO data = calculationRouteService.getByCalculationId(calculation.getId());
            assertEquals(xVal[i], data.getAppliedValue());
            assertEquals(yVal[i], data.getResultValue());

            for (int j = 0; j < 3; j++) {
                assertEquals(functionSets.get(i).get(j).getClass().getSimpleName(), data.getAppliedFunctionData().get(j).getClass().getSimpleName());
            }
        }
    }
}
