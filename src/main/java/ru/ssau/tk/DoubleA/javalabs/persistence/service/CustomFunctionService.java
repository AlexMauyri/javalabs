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

    public List<CustomFunction> getCustomFunctions() {
        return customFunctionDAO.findAll();
    }

    public CustomFunction createFunction(String functionName, byte[] serializedFunction) {
        CustomFunction customFunction = customFunctionDAO.findBySerializedFunction(serializedFunction);
        if (customFunction != null) {
            throw new FunctionAlreadyExists("Эта функция уже существует!!! Её название - " + customFunction.getName());
        }

        return customFunctionDAO.save(new CustomFunction(functionName, serializedFunction));
    }
}
