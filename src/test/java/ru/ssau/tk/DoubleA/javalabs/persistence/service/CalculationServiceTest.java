package ru.ssau.tk.DoubleA.javalabs.persistence.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.ssau.tk.DoubleA.javalabs.bootloader.MathApplication;
import ru.ssau.tk.DoubleA.javalabs.persistence.entity.Calculation;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(classes = MathApplication.class)
public class CalculationServiceTest {

    @Autowired
    private CalculationService calculationService;

    private List<Calculation> createdCalculations = new ArrayList<>();
    private double[] xVal = new double[]{47, -62, 91, -99, -23};
    private double[] yVal = new double[]{-30, -4, -73, 42, 3};
    private long[] hash = new long[]{1,2,3,3,3};

    @AfterEach
    public void tearDown() {
        for (int id = 0; id <= 4; id++) {
            calculationService.deleteById(createdCalculations.get(id).getId());

            Calculation calculation = calculationService.getById(createdCalculations.get(id).getId());
            assertNull(calculation);
        }
        createdCalculations.clear();
    }

    @Test
    void testCreateAndRead() {
        for (int id = 0; id <= 4; id++) {
            Calculation calculation = new Calculation();
            calculation.setAppliedX(xVal[id]);
            calculation.setResultY(yVal[id]);
            calculation.setHash(hash[id]);

            calculationService.create(calculation);
            createdCalculations.add(calculation);
        }

        for (int id = 0; id <= 4; id++) {
            Calculation calculation = calculationService.getById(createdCalculations.get(id).getId());

            Assertions.assertNotNull(calculation);
            Assertions.assertEquals(createdCalculations.get(id).getId(), calculation.getId());
            Assertions.assertEquals(xVal[id], calculation.getAppliedX());
            Assertions.assertEquals(yVal[id], calculation.getResultY());
            Assertions.assertEquals(hash[id], calculation.getHash());
        }

        List<Calculation> calculationByHash = calculationService.getByHash(3);
        Assertions.assertNotNull(calculationByHash);
        Assertions.assertEquals(3, calculationByHash.size());

        for (int id = 0; id <= 2; id++) {
            Assertions.assertNotNull(calculationByHash.get(id));
            Assertions.assertEquals(createdCalculations.get(id+2).getId(), calculationByHash.get(id).getId());
            Assertions.assertEquals(xVal[id+2], calculationByHash.get(id).getAppliedX());
            Assertions.assertEquals(yVal[id+2], calculationByHash.get(id).getResultY());
            Assertions.assertEquals(hash[id+2], calculationByHash.get(id).getHash());
        }

        List<Calculation> calculationsFull = calculationService.getAll();
        Assertions.assertNotNull(calculationsFull);

        for (int id = 0; id <= 4; id++) {
            createdCalculations.get(id).setId(createdCalculations.get(id).getId());
        }
    }

    @Test
    void testUpdateAndDelete() {
        double[] xValNew = new double[]{47, -1, 91, -99, -23};
        double[] yValNew = new double[]{-30, -89, -73, 42, 3};
        long[] hashNew = new long[]{1,2,2,2,2};

        for (int id = 0; id <= 4; id++) {
            Calculation calculation = new Calculation();
            calculation.setAppliedX(xVal[id]);
            calculation.setResultY(yVal[id]);
            calculation.setHash(hash[id]);

            calculationService.create(calculation);
            createdCalculations.add(calculation);
        }

        for (int id = 0; id <= 4; id++) {
            Calculation calculation = calculationService.getById(createdCalculations.get(id).getId());
            calculation.setAppliedX(xValNew[id]);
            calculation.setResultY(yValNew[id]);
            calculation.setHash(hashNew[id]);

            calculationService.update(calculation);
        }

        for (int id = 0; id <= 4; id++) {
            Calculation calculation = calculationService.getById(createdCalculations.get(id).getId());

            Assertions.assertNotNull(calculation);
            Assertions.assertEquals(createdCalculations.get(id).getId(), calculation.getId());
            Assertions.assertEquals(xValNew[id], calculation.getAppliedX());
            Assertions.assertEquals(yValNew[id], calculation.getResultY());
            Assertions.assertEquals(hashNew[id], calculation.getHash());
        }

        List<Calculation> calculationByHash = calculationService.getByHash(2);
        Assertions.assertNotNull(calculationByHash);
        Assertions.assertEquals(4, calculationByHash.size());

        for (int id = 0; id <= 3; id++) {
            Assertions.assertNotNull(calculationByHash.get(id));
            Assertions.assertEquals(createdCalculations.get(id+1).getId(), calculationByHash.get(id).getId());
            Assertions.assertEquals(xValNew[id+1], calculationByHash.get(id).getAppliedX());
            Assertions.assertEquals(yValNew[id+1], calculationByHash.get(id).getResultY());
            Assertions.assertEquals(hashNew[id+1], calculationByHash.get(id).getHash());
        }
    }
}
