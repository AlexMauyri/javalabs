package ru.ssau.tk.DoubleA.javalabs.persistence.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ssau.tk.DoubleA.javalabs.functions.MathFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.StrictTabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.TabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.UnmodifiableTabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.persistence.Operations;
import ru.ssau.tk.DoubleA.javalabs.persistence.Sorting;
import ru.ssau.tk.DoubleA.javalabs.persistence.dao.AppliedFunctionGenericDAOImpl;
import ru.ssau.tk.DoubleA.javalabs.persistence.dao.CalculationGenericDAOImpl;
import ru.ssau.tk.DoubleA.javalabs.persistence.dto.CalculationDataDTO;
import ru.ssau.tk.DoubleA.javalabs.persistence.entity.AppliedFunction;
import ru.ssau.tk.DoubleA.javalabs.persistence.entity.Calculation;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;

@Service
public class CalculationRouteService {

    @Autowired
    private AppliedFunctionGenericDAOImpl appliedFunctionDAO;

    @Autowired
    private CalculationGenericDAOImpl calculationDAO;

    @Transactional
    public void create(double appliedValue, double resultValue, List<MathFunction> appliedFunctionData) {
        try {
            Calculation calculation = new Calculation();
            calculation.setAppliedX(appliedValue);
            calculation.setResultY(resultValue);
            calculation.setHash(computeHash(appliedFunctionData));
            calculationDAO.create(calculation);

            for (int i = 0; i < appliedFunctionData.size(); i++) {
                MathFunction function = appliedFunctionData.get(i);

                AppliedFunction appliedFunction = new AppliedFunction();
                appliedFunction.setCalculationId(calculation);
                appliedFunction.setFunctionOrder(i + 1); // Start from 1
                appliedFunction.setFunctionSerialized(serializeFunction(function));
                if (function instanceof TabulatedFunction) {
                    appliedFunction.setModStrict(function instanceof StrictTabulatedFunction);
                    appliedFunction.setModUnmodifiable(function instanceof UnmodifiableTabulatedFunction);
                }
                appliedFunctionDAO.create(appliedFunction);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public CalculationDataDTO getByCalculationId(int calculationId) throws IllegalArgumentException {
        Calculation calculation = calculationDAO.read(calculationId);
        if (calculation == null) {
            throw new IllegalArgumentException("Calculation not found for id: " + calculationId);
        }

        List<AppliedFunction> appliedFunctions = appliedFunctionDAO.readByCalculationId(calculationId);

        List<MathFunction> appliedFunctionData = new ArrayList<>();
        for (AppliedFunction appliedFunction : appliedFunctions) {
            MathFunction function = deserializeFunction(appliedFunction.getFunctionSerialized());
            if (function instanceof TabulatedFunction) {
                if (appliedFunction.getModStrict())
                    function = new StrictTabulatedFunction((TabulatedFunction) function);
                if (appliedFunction.getModUnmodifiable())
                    function = new UnmodifiableTabulatedFunction((TabulatedFunction) function);
            }
            appliedFunctionData.add(function);
        }

        return new CalculationDataDTO(calculation.getAppliedX(), calculation.getResultY(), appliedFunctionData);
    }

    @Transactional
    public List<CalculationDataDTO> getAllByFilter(Double appliedValue, Double resultValue,
                                                   Operations operationX, Operations operationY,
                                                   Sorting sortingX, Sorting sortingY) {
        List<Calculation> calculations = calculationDAO.readAll(
                appliedValue,   resultValue,
                operationX,     operationY,
                sortingX,       sortingY
        );

        List<CalculationDataDTO> calculationDataDTOs = new ArrayList<>();
        for (Calculation calculation : calculations) {
            calculationDataDTOs.add(this.getByCalculationId(calculation.getId()));
        }

        return calculationDataDTOs;
    }

    @Transactional
    public Calculation getByAppliedValueAndRoute(double appliedValue, List<MathFunction> appliedFunctionData) {
        long hash = computeHash(appliedFunctionData);
        List<Calculation> calculations = calculationDAO.readByHash(hash);

        for (Calculation calculation : calculations) {
            if (calculation.getAppliedX() == appliedValue) {
                return calculation;
            }
        }
        return null;
    }

    private byte[] serializeFunction(MathFunction function) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(bos)) {
            out.writeObject(function);
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to serialize function", e);
        }
    }

    private MathFunction deserializeFunction(byte[] data) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            return (MathFunction) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to deserialize function", e);
        }
    }

    public long computeHash(List<MathFunction> appliedFunctionData) {
        CRC32 crc = new CRC32();
        for (MathFunction function : appliedFunctionData) {
            crc.update(function.hashCode());
        }
        return crc.getValue();
    }
}