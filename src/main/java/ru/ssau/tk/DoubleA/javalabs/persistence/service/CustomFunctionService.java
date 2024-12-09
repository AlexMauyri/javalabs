package ru.ssau.tk.DoubleA.javalabs.persistence.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ssau.tk.DoubleA.javalabs.exceptions.FunctionAlreadyExists;
import ru.ssau.tk.DoubleA.javalabs.persistence.dao.CustomFunctionDAO;
import ru.ssau.tk.DoubleA.javalabs.persistence.entity.CustomFunction;

import java.util.List;

@Service
public class CustomFunctionService {

    private final CustomFunctionDAO customFunctionDAO;

    public CustomFunctionService(@Autowired CustomFunctionDAO customFunctionDAO) {
        this.customFunctionDAO = customFunctionDAO;
    }

    public List<CustomFunction> getCustomFunctionsByUserId(int userId) {
        return customFunctionDAO.findAllByUserId(userId);
    }

    public CustomFunction createFunction(int userId, String functionName, byte[] serializedFunction) {
        CustomFunction customFunction = customFunctionDAO.findBySerializedFunctionAndUserId(serializedFunction, userId);
        if (customFunction != null) {
            throw new FunctionAlreadyExists("Эта функция уже существует!!! Её название - " + customFunction.getName());
        }

        return customFunctionDAO.save(new CustomFunction(userId, functionName, serializedFunction));
    }
}
