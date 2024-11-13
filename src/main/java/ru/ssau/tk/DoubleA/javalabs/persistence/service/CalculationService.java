package ru.ssau.tk.DoubleA.javalabs.persistence.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ssau.tk.DoubleA.javalabs.persistence.Operations;
import ru.ssau.tk.DoubleA.javalabs.persistence.Sorting;
import ru.ssau.tk.DoubleA.javalabs.persistence.dao.CalculationGenericDAOImpl;
import ru.ssau.tk.DoubleA.javalabs.persistence.entity.Calculation;

import java.util.List;

@Service
public class CalculationService implements GenericService<Calculation> {

    @Autowired
    private CalculationGenericDAOImpl calculationDAO;

    @Override
    @Transactional
    public Calculation getById(int id) {
        return calculationDAO.read(id);
    }

    @Transactional
    public List<Calculation> getByHash(long hash) {
        return calculationDAO.readByHash(hash);
    }

    @Transactional
    public List<Calculation> getAllByFilter(Double appliedValue, Double resultValue,
                                            Operations operationX, Operations operationY,
                                            Sorting sortingX, Sorting sortingY) {
        return calculationDAO.readAll(appliedValue, resultValue, operationX, operationY, sortingX, sortingY);
    }

    @Override
    @Transactional
    public List<Calculation> getAll() {
        return calculationDAO.readAll();
    }

    @Override
    @Transactional
    public void create(Calculation employee) {
        calculationDAO.create(employee);
    }

    @Override
    @Transactional
    public void update(Calculation employee) {
        calculationDAO.update(employee);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        calculationDAO.delete(id);
    }
}
