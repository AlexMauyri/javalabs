package ru.ssau.tk.DoubleA.javalabs.persistence.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ssau.tk.DoubleA.javalabs.persistence.entity.AppliedFunction;
import ru.ssau.tk.DoubleA.javalabs.persistence.entity.Calculation;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AppliedFunctionDAOImplTest {
    private CalculationDAOImpl calculationDAO = new CalculationDAOImpl();
    private AppliedFunctionDAOImpl appliedFunctionDAO = new AppliedFunctionDAOImpl();
    private Calculation calculation;
    private List<AppliedFunction> functions = new ArrayList<>();
    private byte[] testSerializedFunction = new byte[]{0, 1, 2, 3, 4};

    @BeforeEach
    public void setUp() {
        calculation = new Calculation();
        calculation.setAppliedX(5.25);
        calculation.setResultY(12.07);
        calculation.setHash(123123123123L);
        calculationDAO.create(calculation);

        for (int id = 1; id <= 5; id++) {
            AppliedFunction appliedFunction = new AppliedFunction();
            appliedFunction.setCalculationId(calculation);
            appliedFunction.setFunctionOrder(id);
            appliedFunction.setFunctionSerialized(testSerializedFunction);
            if (id == 4) {
                appliedFunction.setModStrict(false);
                appliedFunction.setModUnmodifiable(true);
            }
            if (id == 5) {
                appliedFunction.setModStrict(true);
                appliedFunction.setModUnmodifiable(false);
            }
            appliedFunctionDAO.create(appliedFunction);
            functions.add(appliedFunction);
        }
    }

    @AfterEach
    public void tearDown() {
        calculationDAO.delete(calculation.getId());
        functions.clear();
    }

    @Test
    void testCreateAndRead() {
        for (int id = 6; id <= 10; id++) {
            AppliedFunction appliedFunction = new AppliedFunction();
            appliedFunction.setCalculationId(calculation);
            appliedFunction.setFunctionOrder(id);
            appliedFunction.setFunctionSerialized(testSerializedFunction);
            if (id == 9) {
                appliedFunction.setModStrict(false);
                appliedFunction.setModUnmodifiable(true);
            }
            if (id == 10) {
                appliedFunction.setModStrict(true);
                appliedFunction.setModUnmodifiable(false);
            }
            appliedFunctionDAO.create(appliedFunction);
            functions.add(appliedFunction);
        }

        List<AppliedFunction> appliedFunctions = appliedFunctionDAO.readByCalculationId(calculation.getId());
        assertNotNull(appliedFunctions);
        assertEquals(10, appliedFunctions.size());

        List<AppliedFunction> appliedFunctionsFull = appliedFunctionDAO.readAll();
        assertNotNull(appliedFunctionsFull);

        for (int id = 6; id <= 10; id++) {
            AppliedFunction appliedFunction = appliedFunctionDAO.read(functions.get(id-1).getId());

            assertNotNull(appliedFunction);
            assertEquals(functions.get(id-1).getId(), appliedFunction.getId());
            assertEquals(calculation.getId(), appliedFunction.getCalculationId().getId());
            assertEquals(id, appliedFunction.getFunctionOrder());
            assertEquals(testSerializedFunction[id-6], appliedFunction.getFunctionSerialized()[id-6]);
            if (id == 9) {
                assertEquals(false, appliedFunction.getModStrict());
                assertEquals(true, appliedFunction.getModUnmodifiable());
            }
            if (id == 10) {
                assertEquals(true, appliedFunction.getModStrict());
                assertEquals(false, appliedFunction.getModUnmodifiable());
            }
        }

        for (int id = 0; id <= 4; id++) {
            functions.get(id).setId(id);
        }
    }

    @Test
    void testUpdateAndDelete() {
        byte[] testSerializedFunction2 = new byte[]{5, 6, 7, 8, 9};

        for (int id = 1; id <= 5; id++) {
            AppliedFunction appliedFunction = appliedFunctionDAO.read(functions.get(id-1).getId());
            appliedFunction.setFunctionOrder(id+12);
            appliedFunction.setFunctionSerialized(testSerializedFunction2);
            if (id == 4) {
                appliedFunction.setModStrict(true);
                appliedFunction.setModUnmodifiable(false);
            }
            if (id == 5) {
                appliedFunction.setModStrict(false);
                appliedFunction.setModUnmodifiable(true);
            }
            appliedFunctionDAO.update(appliedFunction);
        }

        for (int id = 1; id <= 5; id++) {
            AppliedFunction appliedFunction = appliedFunctionDAO.read(functions.get(id-1).getId());

            assertNotNull(appliedFunction);
            assertEquals(functions.get(id-1).getId(), appliedFunction.getId());
            assertEquals(calculation.getId(), appliedFunction.getCalculationId().getId());
            assertEquals(id+12, appliedFunction.getFunctionOrder());
            assertEquals(testSerializedFunction2[id-1], appliedFunction.getFunctionSerialized()[id-1]);
            if (id == 4) {
                assertEquals(true, appliedFunction.getModStrict());
                assertEquals(false, appliedFunction.getModUnmodifiable());
            }
            if (id == 5) {
                assertEquals(false, appliedFunction.getModStrict());
                assertEquals(true, appliedFunction.getModUnmodifiable());
            }

            appliedFunctionDAO.delete(appliedFunction.getId());
            appliedFunction = appliedFunctionDAO.read(appliedFunction.getId());
            assertNull(appliedFunction);
            appliedFunction = appliedFunctionDAO.read(functions.get(id-1).getId());
            assertNull(appliedFunction);
        }
    }
}
