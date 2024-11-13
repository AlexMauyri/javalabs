package ru.ssau.tk.DoubleA.javalabs.persistence.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ssau.tk.DoubleA.javalabs.persistence.dao.AppliedFunctionGenericDAOImpl;
import ru.ssau.tk.DoubleA.javalabs.persistence.entity.AppliedFunction;

import java.util.List;

@Service
public class AppliedFunctionService implements GenericService<AppliedFunction> {

    @Autowired
    private AppliedFunctionGenericDAOImpl appliedFunctionDAO;

    @Override
    @Transactional
    public AppliedFunction getById(int id) {
        return appliedFunctionDAO.read(id);
    }

    @Transactional
    public List<AppliedFunction> getByCalculationId(int id) {
        return appliedFunctionDAO.readByCalculationId(id);
    }

    @Override
    @Transactional
    public List<AppliedFunction> getAll() {
        return appliedFunctionDAO.readAll();
    }

    @Override
    @Transactional
    public void create(AppliedFunction employee) {
        appliedFunctionDAO.create(employee);
    }

    @Override
    @Transactional
    public void update(AppliedFunction employee) {
        appliedFunctionDAO.update(employee);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        appliedFunctionDAO.delete(id);
    }
}
